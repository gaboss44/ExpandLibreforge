package com.github.gaboss44.expandlibreforge.bootstrap;

public final class ClassloaderValidator {
    private ClassloaderValidator() {}

    /**
     * Returns true if the class is currently loaded by the bootstrap classloader (loader == null).
     * If the class is not loaded, this will attempt to load it using bootstrap classloader.
     */
    public static boolean ensureLoadedByBootstrap(String className) {
        try {
            Class<?> c = Class.forName(className, false, null); // null => bootstrap CL
            return c.getClassLoader() == null;
        } catch (Throwable t) {
            return false;
        }
    }
}
