package com.mav.buildscale.plugin.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty(value = "SUCCESS") SUCCESS,
    @JsonProperty(value = "FAILED") FAILED
}
