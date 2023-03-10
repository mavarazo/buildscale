package com.mav.buildscale.plugin.internal.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.gradle.tooling.events.OperationResult;
import org.gradle.tooling.events.task.TaskFailureResult;
import org.gradle.tooling.events.task.TaskSkippedResult;
import org.gradle.tooling.events.task.TaskSuccessResult;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Task {

    private String path;
    private long startTime;
    private long endTime;
    private long durationInMillis;

    @Builder.Default
    private TaskStatus status = TaskStatus.EXECUTED;
    private boolean isIncremental;
    private final List<String> messages = new ArrayList<>();

    public static Task of(final String name, final OperationResult operationResult) {
        if (operationResult instanceof TaskSuccessResult successResult) {
            return Task.of(name, successResult);
        } else if (operationResult instanceof TaskSkippedResult skippedResult) {
            return Task.of(name, skippedResult);
        } else if (operationResult instanceof TaskFailureResult failureResult) {
            return Task.of(name, failureResult);
        }
        throw new IllegalArgumentException("Unhandled OperationResult");
    }

    private static Task of(final String name, final TaskSuccessResult successResult) {
        final Task task = Task.builder()
                .path(name)
                .startTime(successResult.getStartTime())
                .endTime(successResult.getEndTime())
                .status(getTaskStatus(successResult))
                .isIncremental(successResult.isIncremental())
                .build();

        if (nonNull(successResult.getExecutionReasons())) {
            successResult.getExecutionReasons().forEach(task::addMessage);
        }
        return task;
    }

    private static TaskStatus getTaskStatus(final TaskSuccessResult successResult) {
        if (successResult.isUpToDate()) {
            return TaskStatus.UP_TO_DATE;
        } else if (successResult.isFromCache()) {
            return TaskStatus.FROM_CACHE;
        }
        return TaskStatus.EXECUTED;
    }

    private static Task of(final String name, final TaskSkippedResult skippedResult) {
        final Task task = Task.builder()
                .path(name)
                .startTime(skippedResult.getStartTime())
                .endTime(skippedResult.getEndTime())
                .status(TaskStatus.SKIPPED)
                .build();
        task.addMessage(skippedResult.getSkipMessage());
        return task;
    }

    private static Task of(final String name, final TaskFailureResult failureResult) {
        final Task task = Task.builder()
                .path(name)
                .startTime(failureResult.getStartTime())
                .endTime(failureResult.getEndTime())
                .status(TaskStatus.FAILED)
                .build();
        if (nonNull(failureResult.getExecutionReasons())) {
            failureResult.getExecutionReasons().forEach(task::addMessage);
        }
        return task;
    }

    @JsonGetter("durationInMillis")
    public long getDurationInMillis() {
        return endTime - startTime;
    }

    public void addMessage(final String message) {
        messages.add(message);
    }
}
