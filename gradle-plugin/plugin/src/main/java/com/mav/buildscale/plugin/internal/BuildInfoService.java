package com.mav.buildscale.plugin.internal;

import com.mav.buildscale.plugin.internal.model.Report;
import com.mav.buildscale.plugin.internal.model.Tag;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * collect info about the build system
 */
public abstract class BuildInfoService extends AbstractBuildService implements BuildService<BuildInfoService.Params> {


    public interface Params extends BuildServiceParameters {

        Property<String> getGradleVersion();

        Property<Provider<BuildPublishService>> getBuildPublishServiceProvider();
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

    @Override
    public void close() {
        final Report report = getParameters().getBuildPublishServiceProvider().get().get().getReport();
        Stream.of(collectBasicInfo(), collectJavaInfo(), collectGradleInfo())
                .flatMap(m -> m.entrySet().stream())
                .map(e -> Tag.of(e.getKey(), e.getValue()))
                .forEach(report::addTag);
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
