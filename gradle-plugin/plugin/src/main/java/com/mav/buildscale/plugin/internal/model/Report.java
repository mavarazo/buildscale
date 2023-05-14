package com.mav.buildscale.plugin.internal.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Report implements Serializable {

    private String project;
    private String hostname;
    private long durationInMillis;

    @Builder.Default
    private Status status = Status.SUCCESS;
    private final List<Task> tasks = new ArrayList<>();
    private final List<Tag> tags = new ArrayList<>();
    private final List<Test> tests = new ArrayList<>();

    public void addTask(final Task task) {
        tasks.add(task);
    }

    public void addTag(final Tag tag) {
        tags.add(tag);
    }

    public void addTest(final Test test) {
        tests.add(test);
    }

    public long getDurationInMillis() {
        return tasks.stream()
                .mapToLong(Task::getDurationInMillis)
                .sum();
    }

    public String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (final Exception ex) {
            return null;
        }
    }

    public Status getStatus() {
        return tasks.stream().anyMatch(t -> TaskStatus.FAILED.equals(t.getStatus())) ? Status.FAILED : Status.SUCCESS;
    }
}
