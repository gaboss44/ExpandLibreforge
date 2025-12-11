package com.github.gaboss44.expandlibreforge.bootstrap.agent;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public final class AgentBridge {

    private static final Set<ClassLoader> pluginClassLoaders =
            Collections.newSetFromMap(new IdentityHashMap<>());

    private AgentBridge() {}

    public static void registerPluginClassLoader(ClassLoader loader) {
        if (loader != null) {
            pluginClassLoaders.add(loader);
        }
    }

    public static Set<ClassLoader> getPluginClassLoaders() {
        return Collections.unmodifiableSet(pluginClassLoaders);
    }
}
