package com.mav.buildscale.plugin.internal;

import com.mav.buildscale.plugin.internal.model.Report;
import com.mav.buildscale.plugin.internal.model.Task;
import lombok.Getter;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;
import org.gradle.tooling.events.FinishEvent;
import org.gradle.tooling.events.task.TaskFinishEvent;

/**
 * collect info about finished tasks
 */
public abstract class BuildReportService extends AbstractBuildService implements BuildService<BuildServiceParameters.None> {

    @Getter
    private final Report report = new Report();

    @Override
    public void onFinish(final FinishEvent event) {
        if (event instanceof TaskFinishEvent taskFinishEvent) {
            final Task task = Task.of(taskFinishEvent.getDescriptor().getName(), taskFinishEvent.getResult());
            report.addTask(task);
        }

    }
}
