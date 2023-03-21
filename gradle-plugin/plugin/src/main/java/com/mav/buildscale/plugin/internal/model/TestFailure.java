package com.mav.buildscale.plugin.internal.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestFailure {

    private String message;
    private String stacktrace;

    public static TestFailure of(final String message, final String stacktrace) {
        return new TestFailure(message, stacktrace);
    }

}
