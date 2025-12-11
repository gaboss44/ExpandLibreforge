package com.github.gaboss44.expandlibreforge.bootstrap;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bootstrap initializer: forces loading of classes in the bootstrap CL
 * and validates that they were loaded there.
 * Usage: call this AFTER appendToBootstrapClassLoaderSearch(jarFile) was invoked.
 */
public final class BootstrapInitializer {
    private static volatile boolean initialized = false;

    private BootstrapInitializer() {}

    /**
     * Initialize bootstrap module.
     *
     * @param logger      the logger (java.util.logging.Logger)
     */
    public static synchronized void initialize(Logger logger) {
        Objects.requireNonNull(logger, "logger cannot be null");

        if (initialized) {
            logger.info("[Bootstrap] initialize() called but bootstrap already initialized.");
            return;
        }

        initialized = true;

        try {
            ClassLoader thisLoader = BootstrapInitializer.class.getClassLoader();
            logger.info("[Bootstrap] BootstrapInitializer.class.getClassLoader() = " + thisLoader);
        } catch (Throwable t) {
            logger.log(Level.FINE, "[Bootstrap] could not get class loader", t);
        }

        // Force load each class by name using the bootstrap classloader (null)
        String[] names = BootstrapRegistry.classNames();
        for (String name : names) {
            try {
                logger.info("[Bootstrap] Forcing load: " + name);
                // true = initialize, null => bootstrap CL
                Class.forName(name, true, null);
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "[Bootstrap] Failed to load bootstrap class: " + name, t);
                throw new IllegalStateException("Failed to load bootstrap class " + name, t);
            }
        }

        // Validate they are loaded by bootstrap (classLoader == null)
        for (String name : names) {
            try {
                Class<?> c = Class.forName(name, false, null);
                if (c.getClassLoader() != null) {
                    logger.severe("[Bootstrap] Validation failed - class not loaded by bootstrap: " + name
                            + " (loader=" + c.getClassLoader() + ")");
                    throw new IllegalStateException("Class not loaded by bootstrap: " + name);
                } else {
                    logger.info("[Bootstrap] OK (bootstrap): " + name);
                }
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "[Bootstrap] Validation exception for: " + name, t);
                throw new IllegalStateException("Validation failed for " + name, t);
            }
        }

        logger.info("[Bootstrap] Initialization complete. All classes loaded in bootstrap.");
    }
}
