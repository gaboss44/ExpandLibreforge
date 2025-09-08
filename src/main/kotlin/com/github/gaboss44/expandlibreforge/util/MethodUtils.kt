package com.github.gaboss44.expandlibreforge.util

import java.lang.reflect.Method

object MethodUtils {

    fun hasMethod(clazz: Class<*>, methodName: String, vararg parameterTypes: Class<*>): Boolean {
        return try {
            clazz.getMethod(methodName, *parameterTypes)
            true
        } catch (_: NoSuchMethodException) {
            false
        } catch (_: SecurityException) {
            false
        }
    }

    fun getMethod(clazz: Class<*>, methodName: String, vararg parameterTypes: Class<*>): Method? {
        return try {
            clazz.getMethod(methodName, *parameterTypes)
        } catch (_: Exception) {
            null
        }
    }
}