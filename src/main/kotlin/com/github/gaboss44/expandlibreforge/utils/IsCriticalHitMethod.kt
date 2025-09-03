package com.github.gaboss44.expandlibreforge.utils

import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.lang.reflect.Method

object IsCriticalHitMethod {
    private var method: Method? = null

    init {
        method = try {
            EntityDamageByEntityEvent::class.java.getMethod("isCritical")
        } catch (_: NoSuchMethodException) {
            null
        }
    }

    fun invoke(event: EntityDamageByEntityEvent): Boolean {
        method?.let {
            return try {
                it.invoke(event) as Boolean
            } catch (_: Exception) {
                false
            }
        }
        return false
    }
}