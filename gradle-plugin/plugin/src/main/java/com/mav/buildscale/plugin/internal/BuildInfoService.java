package com.mav.buildscale.plugin.internal;

import org.gradle.api.provider.Property;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * collect info about the build system
 */
public abstract class BuildInfoService implements BuildService<BuildInfoService.Params> {


    public interface Params extends BuildServiceParameters {
        Property<String> getProjectName();
        Property<String> getGradleVersion();
    }

    private static final List<String> BASIC_SYSTEM_PROPERTIES = new ArrayList<>(List.of(
            "os.arch",
            "os.name",
            "os.version"
    ));

    private static final List<String> JAVA_SYSTEM_PROPERTIES = new ArrayList<>(List.of(
            "java.vendor",
            "java.version"
    ));

    public Optional<String> getProjectName() {
        return Optional.ofNullable(getParameters().getProjectName().getOrNull());
    }

    public Optional<String> getHostname() {
        try {
            return Optional.ofNullable(InetAddress.getLocalHost().getHostName());
        } catch (final Exception ex) {
            return Optional.empty();
        }
    }

    public Map<String, String> collectBasicInfo() {
        final Map<String, String> result = new HashMap<>();

        BASIC_SYSTEM_PROPERTIES
                .forEach(k -> getSystemProperty(k).ifPresent(v -> result.put(k, v)));

        return result;
    }

    public Map<String, String> collectJavaInfo() {
        final Map<String, String> result = new HashMap<>();

        JAVA_SYSTEM_PROPERTIES
                .forEach(k -> getSystemProperty(k).ifPresent(v -> result.put(k, v)));

        result.put("java.cpu", String.valueOf(Runtime.getRuntime().availableProcessors()));
        result.put("java.memory.free", String.valueOf(Runtime.getRuntime().freeMemory()));
        result.put("java.memory.total", String.valueOf(Runtime.getRuntime().totalMemory()));
        result.put("java.memory.max", String.valueOf(Runtime.getRuntime().maxMemory()));

        return result;
    }

    public Map<String, String> collectGradleInfo() {
        final Map<String, String> result = new HashMap<>();
        if (getParameters().getGradleVersion().isPresent()) {
            result.put("gradle.version", getParameters().getGradleVersion().get());
        }
        return result;
    }

    private static Optional<String> getSystemProperty(final String property) {
        try {
            return Optional.ofNullable(System.getProperty(property));
        } catch (final Exception ex) {
            return Optional.empty();
        }
    }
}
