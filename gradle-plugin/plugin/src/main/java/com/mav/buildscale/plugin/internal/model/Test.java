package com.mav.buildscale.plugin.internal.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Test {

    private String name;

    private String className;

    private long durationInMillis;

    @Builder.Default
    private final TestStatus status = TestStatus.SUCCESS;

    private final List<TestFailure> failures = new ArrayList<>();

    public void addTestFailure(final TestFailure testFailure) {
        failures.add(testFailure);
    }
}
