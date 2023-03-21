package com.mav.buildscale.plugin.internal;

import com.mav.buildscale.plugin.internal.model.Test;
import com.mav.buildscale.plugin.internal.model.TestFailure;
import com.mav.buildscale.plugin.internal.model.TestStatus;
import lombok.RequiredArgsConstructor;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.testing.TestDescriptor;
import org.gradle.api.tasks.testing.TestListener;
import org.gradle.api.tasks.testing.TestResult;

@RequiredArgsConstructor
public class TestReportService implements TestListener {

    private final Provider<BuildPublishService> buildPublishServiceProvider;

    @Override
    public void beforeSuite(final TestDescriptor suite) {
        // nothing
    }

    @Override
    public void afterSuite(final TestDescriptor suite, final TestResult result) {
        // nothing
    }

    @Override
    public void beforeTest(final TestDescriptor testDescriptor) {
        // nothing
    }

    @Override
    public void afterTest(final TestDescriptor testDescriptor, final TestResult result) {
        final Test test = Test.builder()
                .name(testDescriptor.getName())
                .className(testDescriptor.getClassName())
                .durationInMillis(result.getEndTime() - result.getStartTime())
                .status(getTestStatus(result.getResultType()))
                .build();

        result.getFailures().stream()
                .map(org.gradle.api.tasks.testing.TestFailure::getDetails)
                .map(details -> TestFailure.of(details.getMessage(), details.getStacktrace()))
                .forEach(test::addTestFailure);

        buildPublishServiceProvider.get().getReport().addTest(test);
    }

    private TestStatus getTestStatus(final TestResult.ResultType resultType) {
        return switch (resultType) {
            case SUCCESS -> TestStatus.SUCCESS;
            case FAILURE -> TestStatus.FAILED;
            case SKIPPED -> TestStatus.SKIPPED;
        };
    }
}
