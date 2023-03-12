package com.mav.buildscale.model;

import lombok.Data;

@Data
public class CreateTask {

    private String path;
    private Long startTime;
    private Long endTime;
    private Long durationInMillis;
    private TaskStatus status = TaskStatus.EXECUTED;
}
