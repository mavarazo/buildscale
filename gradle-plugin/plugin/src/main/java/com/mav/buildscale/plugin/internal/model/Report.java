package com.mav.buildscale.plugin.internal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Report {

    private String project;
    private String hostname;
    private long durationInMillis;
    private final List<Task> tasks = new ArrayList<>();
    private final List<Tag> tags = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private final long startTime = System.currentTimeMillis();

    public void addTask(final Task task) {
        tasks.add(task);
        calculateDurationInMillis();
    }

    public void addTag(final Tag tag) {
        tags.add(tag);
    }

    private void calculateDurationInMillis() {
        durationInMillis = System.currentTimeMillis() - startTime;
    }
}
