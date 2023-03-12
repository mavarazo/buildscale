package com.mav.buildscale.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateReport {

    private String project;
    private String hostname;
    private Long durationInMillis;
    private LocalDateTime created;
    private List<CreateTask> tasks = new ArrayList<>();
    private List<CreateTag> tags = new ArrayList<>();
    

}
