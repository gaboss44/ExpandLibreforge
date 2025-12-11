package com.github.gaboss44.expandlibreforge.bootstrap.agent.advice;

import com.github.gaboss44.expandlibreforge.bootstrap.agent.AgentBridge;

import java.lang.reflect.Constructor;

public final class PluginInvoker {

    private PluginInvoker() {}

    public static MethodDispatch createDispatch(String fqcn) {
        for (ClassLoader loader : AgentBridge.getPluginClassLoaders()) {
            try {
                Class<?> clazz = Class.forName(fqcn, true, loader);
                if (!MethodDispatch.class.isAssignableFrom(clazz)) {
                    continue;
                }

                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                return (MethodDispatch) constructor.newInstance();

            } catch (Throwable ignored) {
                // Try next classloader
            }
        }

        return null;
    }
}
