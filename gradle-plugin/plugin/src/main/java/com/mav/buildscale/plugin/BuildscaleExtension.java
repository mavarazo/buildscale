package com.mav.buildscale.plugin;

import lombok.Getter;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

import java.net.URL;

@Getter
public class BuildscaleExtension {

    private final Property<Boolean> publishEnabled;
    private final Property<URL> uri;
    private final Property<Boolean> verboseEnabled;

    public BuildscaleExtension(final ObjectFactory objects) {
        publishEnabled = objects.property(Boolean.class).convention(Boolean.TRUE);
        uri = objects.property(URL.class);
        verboseEnabled = objects.property(Boolean.class).convention(Boolean.FALSE);
    }
}
