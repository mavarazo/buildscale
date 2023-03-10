package com.mav.buildscale.plugin.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskStatus {
    @JsonProperty(value = "EXECUTED") EXECUTED,
    @JsonProperty(value = "UP-TO-DATE") UP_TO_DATE,
    @JsonProperty(value = "FROM-CACHE") FROM_CACHE,
    @JsonProperty(value = "SKIPPED") SKIPPED,
    @JsonProperty(value = "FAILED") FAILED;
}
