package com.mav.buildscale.plugin.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mav.buildscale.plugin.internal.model.Report;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.provider.Property;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

/**
 * merge and publish report and info
 */
public abstract class BuildPublishService extends AbstractBuildService implements BuildService<BuildPublishService.Params> {

    public interface Params extends BuildServiceParameters {

        Property<Boolean> getPublishEnabled();

        Property<URL> getUrl();

        Property<Boolean> getVerboseEnabled();

        Property<Report> getReport();
    }

    private static final Logger LOGGER = Logging.getLogger(BuildPublishService.class);

    public Report getReport() {
        return getParameters().getReport().get();
    }

    @Override
    public void close() throws Exception {
        synchronized (getReport()) {
            final Report report = getReport();
            String data = new ObjectMapper().writeValueAsString(report);
            if (getParameters().getPublishEnabled().getOrElse(Boolean.FALSE).equals(Boolean.TRUE)
                    && getParameters().getUrl().isPresent()
                    && report.getDurationInMillis() > 0) {
                LOGGER.lifecycle("Publishing build report...");
                publish(getParameters().getUrl().get(), data);
            }

            if (getParameters().getVerboseEnabled().getOrElse(Boolean.FALSE).equals(Boolean.TRUE)) {
                LOGGER.lifecycle("Build report: {}", data);
            }
        }
    }

    private static void publish(URL url, String data) {
        try {
            HttpURLConnection postConnection = (HttpURLConnection) url.openConnection();
            postConnection.setRequestMethod("POST");
            postConnection.setRequestProperty("Content-Type", "application/json");
            postConnection.setDoOutput(true);
            OutputStream os = postConnection.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            LOGGER.lifecycle(postConnection.getResponseMessage());

            if (postConnection.getResponseCode() != 200) {
                LOGGER.warn("Unable to publish build report. status: {}, response: {}, report: {}",
                        postConnection.getResponseCode(), Optional.ofNullable(postConnection.getResponseMessage()).orElse("empty"),
                        data);
            }
        } catch (IOException e) {
            LOGGER.warn("Unable to publish build report. {}, report: {}",
                    e.getMessage(),
                    data);
        }
    }
}
