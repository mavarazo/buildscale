package com.mav.buildscale.plugin.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mav.buildscale.plugin.internal.model.Report;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.provider.Property;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;

import java.net.URL;

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

            if (getParameters().getPublishEnabled().getOrElse(Boolean.FALSE).equals(Boolean.TRUE)
                    && getParameters().getUrl().isPresent()
                    && report.getDurationInMillis() > 0) {
                LOGGER.lifecycle("Publishing build report...");
                new ApiClient(getParameters().getUrl().get()).postReport(report);
            }

            if (getParameters().getVerboseEnabled().getOrElse(Boolean.FALSE).equals(Boolean.TRUE)) {
                final String reportAsJson = new ObjectMapper().writeValueAsString(report);
                LOGGER.lifecycle("Build report: {}", reportAsJson);
            }
        }
    }
}
