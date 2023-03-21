package com.mav.buildscale.plugin.internal;

import com.mav.buildscale.plugin.internal.model.Task;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;
import org.gradle.tooling.events.FinishEvent;
import org.gradle.tooling.events.task.TaskFinishEvent;

/**
 * collect info about finished tasks
 */
public abstract class BuildReportService extends AbstractBuildService implements BuildService<BuildReportService.Params> {

    public interface Params extends BuildServiceParameters {

        Property<Provider<BuildPublishService>> getBuildPublishServiceProvider();
    }
    
    @Override
    public void onFinish(final FinishEvent event) {
        if (event instanceof TaskFinishEvent taskFinishEvent) {
            final Task task = Task.of(taskFinishEvent.getDescriptor().getName(), taskFinishEvent.getResult());
            getParameters().getBuildPublishServiceProvider().get().get().getReport().addTask(task);
        }
    }
}
