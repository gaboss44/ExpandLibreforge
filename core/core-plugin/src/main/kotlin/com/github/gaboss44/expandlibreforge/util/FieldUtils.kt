package com.github.gaboss44.expandlibreforge.util

import java.lang.reflect.Field

object FieldUtils {

    fun hasField(clazz: Class<*>, fieldName: String): Boolean {
        return try {
            clazz.getDeclaredField(fieldName)
            true
        } catch (_: NoSuchFieldException) {
            false
        } catch (_: SecurityException) {
            false
        }
    }

    fun getField(clazz: Class<*>, fieldName: String): Field? {
        return try {
            val field = clazz.getDeclaredField(fieldName)
            field.isAccessible = true
            field
        } catch (_: Exception) {
            null
        }
    }

    fun getFieldValue(instance: Any, fieldName: String): Any? {
        return try {
            val field = instance.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field.get(instance)
        } catch (_: Exception) {
            null
        }
    }

    fun setFieldValue(instance: Any, fieldName: String, value: Any?) {
        try {
            val field = instance.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(instance, value)
        } catch (_: Exception) {
        }
    }
}
