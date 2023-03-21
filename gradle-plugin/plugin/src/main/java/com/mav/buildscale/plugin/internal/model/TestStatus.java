package com.mav.buildscale.plugin.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TestStatus {

    @JsonProperty(value = "SUCCESS") SUCCESS,
    @JsonProperty(value = "FAILED") FAILED,
    @JsonProperty(value = "SKIPPED") SKIPPED
}
