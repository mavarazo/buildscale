package com.mav.buildscale.plugin.internal;

import org.gradle.tooling.events.FinishEvent;
import org.gradle.tooling.events.OperationCompletionListener;

public abstract class AbstractBuildService implements OperationCompletionListener, AutoCloseable {

    @Override
    public void onFinish(final FinishEvent event) {
        // unused
    }

    @Override
    public void close() throws Exception {
        // unused
    }
}
