package com.mav.buildscale.plugin.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mav.buildscale.plugin.internal.model.Report;
import com.mav.buildscale.plugin.internal.model.Tag;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;

import java.net.URL;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * merge and publish report and info
 */
public abstract class BuildPublishService extends AbstractBuildService implements BuildService<BuildPublishService.Params> {

    public interface Params extends BuildServiceParameters {
        Property<Boolean> getPublishEnabled();
        Property<URL> getUrl();
        Property<Boolean> getVerboseEnabled();
        Property<Provider<BuildInfoService>> getBuildInfoServiceProvider();
        Property<Provider<BuildReportService>> getBuildReportServiceProvider();
    }

    private static final Logger LOGGER = Logging.getLogger(BuildPublishService.class);

    @Override
    public void close() throws Exception {
        final Optional<BuildReportService> optionalBuildReportService = getBuildReportService();
        if (optionalBuildReportService.isEmpty()) {
            return;
        }

        final Report report = optionalBuildReportService.get().getReport();
        addBuildInfoToReport(report);

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

    private void addBuildInfoToReport(final Report report) {
        getBuildInfoService().ifPresent(buildInfoService -> {
            buildInfoService.getProjectName().ifPresent(report::setProject);
            buildInfoService.getHostname().ifPresent(report::setHostname);

            Stream.of(buildInfoService.collectBasicInfo(), buildInfoService.collectJavaInfo(), buildInfoService.collectGradleInfo())
                    .flatMap(m -> m.entrySet().stream())
                    .map(e -> Tag.of(e.getKey(), e.getValue()))
                    .forEach(report::addTag);
        });
    }

    private Optional<BuildInfoService> getBuildInfoService() {
        return Optional.ofNullable(getParameters().getBuildInfoServiceProvider())
                .map(Provider::get)
                .map(Provider::get);
    }

    private Optional<BuildReportService> getBuildReportService() {
        return Optional.ofNullable(getParameters().getBuildReportServiceProvider())
                .map(Provider::get)
                .map(Provider::get);
    }
}
