/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.mav.buildscale.plugin;

import com.mav.buildscale.plugin.internal.BuildInfoService;
import com.mav.buildscale.plugin.internal.BuildPublishService;
import com.mav.buildscale.plugin.internal.BuildReportService;
import com.mav.buildscale.plugin.internal.TestReportService;
import com.mav.buildscale.plugin.internal.model.Report;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.provider.Provider;
import org.gradle.build.event.BuildEventsListenerRegistry;

import javax.inject.Inject;

public class BuildscalePlugin implements Plugin<Project> {

    private final BuildEventsListenerRegistry registry;

    @Inject
    public BuildscalePlugin(final BuildEventsListenerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void apply(final Project project) {
        final BuildscaleExtension extension = project.getExtensions().create("buildscale", BuildscaleExtension.class, project.getObjects());

        final Report report = Report.builder()
                .project(project.getName())
                .build();

        final Provider<BuildPublishService> buildPublishServiceProvider = registerBuildPublisherService(project.getGradle(), extension, report);

        project.getGradle().addListener(new TestReportService(buildPublishServiceProvider));
        registerBuildInfoService(project.getGradle(), buildPublishServiceProvider);
        registerBuildReportService(project.getGradle(), buildPublishServiceProvider);
    }

    private void registerBuildInfoService(final Gradle gradle, final Provider<BuildPublishService> buildPublishServiceProvider) {
        final Provider<BuildInfoService> buildInfoServiceProvider = gradle.getSharedServices().registerIfAbsent("build-info-service", BuildInfoService.class, service -> {
            service.getParameters().getGradleVersion().set(gradle.getGradleVersion());
            service.getParameters().getBuildPublishServiceProvider().set(buildPublishServiceProvider);
        });
        registry.onTaskCompletion(buildInfoServiceProvider);
    }

    private void registerBuildReportService(final Gradle gradle, final Provider<BuildPublishService> buildPublishServiceProvider) {
        final Provider<BuildReportService> buildReportServiceProvider = gradle.getSharedServices().registerIfAbsent("build-report-service", BuildReportService.class, service ->
                service.getParameters().getBuildPublishServiceProvider().set(buildPublishServiceProvider));
        registry.onTaskCompletion(buildReportServiceProvider);
    }

    private Provider<BuildPublishService> registerBuildPublisherService(final Gradle gradle, final BuildscaleExtension extension, final Report report) {
        final Provider<BuildPublishService> buildPublishService = gradle.getSharedServices().registerIfAbsent("build-publish-service", BuildPublishService.class, service -> {
            service.getParameters().getPublishEnabled().set(extension.getPublishEnabled());
            service.getParameters().getUrl().set(extension.getUri());
            service.getParameters().getVerboseEnabled().set(extension.getVerboseEnabled());
            service.getParameters().getReport().set(report);
        });
        registry.onTaskCompletion(buildPublishService);
        return buildPublishService;
    }
}