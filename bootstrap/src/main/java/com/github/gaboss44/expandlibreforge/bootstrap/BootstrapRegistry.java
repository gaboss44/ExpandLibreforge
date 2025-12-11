package com.github.gaboss44.expandlibreforge.bootstrap;

import com.github.gaboss44.expandlibreforge.bootstrap.agent.AgentBridge;
import com.github.gaboss44.expandlibreforge.bootstrap.annotations.NotNull;
import com.github.gaboss44.expandlibreforge.bootstrap.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Registry of classes that MUST be loaded in bootstrap classloader.
 * Keep this list in-sync with the classes included in the bootstrap JAR.
 */
public final class BootstrapRegistry {
    private BootstrapRegistry() {}

    public static final List<Class<?>> CLASSES = List.of(
            Nullable.class,
            NotNull.class,
            AgentBridge.class
    );

    public static String[] classNames() {
        return CLASSES.stream()
                .filter(Objects::nonNull)
                .map(Class::getName)
                .toArray(String[]::new);
    }
}
