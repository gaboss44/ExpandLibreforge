package com.github.gaboss44.expandlibreforge.proxies

import org.bukkit.damage.DamageSource
import org.bukkit.event.entity.EntityDamageEvent

@Suppress("UnstableApiUsage")
interface DamageSourceAccessorProxy {

    fun toCritical(
        source: DamageSource
    ): DamageSource

    fun toKnownCause(
        source: DamageSource,
        cause: EntityDamageEvent.DamageCause
    ): DamageSource
}