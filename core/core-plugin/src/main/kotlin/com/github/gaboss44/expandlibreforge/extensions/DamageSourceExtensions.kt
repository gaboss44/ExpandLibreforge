@file:Suppress("UnstableApiUsage")
package com.github.gaboss44.expandlibreforge.extensions

import com.github.gaboss44.expandlibreforge.proxies.DamageSourceAccessorProxy
import org.bukkit.damage.DamageSource
import org.bukkit.event.entity.EntityDamageEvent

private lateinit var damageSourceAccessor: DamageSourceAccessorProxy

object DamageSourceExtensions {

    fun setProxyIfNeeded(proxy: DamageSourceAccessorProxy) {
        if (!::damageSourceAccessor.isInitialized) {
            damageSourceAccessor = proxy
        }
    }
}

fun DamageSource.toCritical() = damageSourceAccessor.toCritical(this)

fun DamageSource.toKnownCause(cause: EntityDamageEvent.DamageCause) =
    damageSourceAccessor.toKnownCause(this, cause)