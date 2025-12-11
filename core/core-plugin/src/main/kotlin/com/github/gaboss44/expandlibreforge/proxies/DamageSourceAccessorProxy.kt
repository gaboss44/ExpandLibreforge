package com.github.gaboss44.expandlibreforge.proxies

import org.bukkit.damage.DamageSource
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface DamageSourceAccessorProxy {

    fun getWeapon(
        source: DamageSource
    ): ItemStack?

    fun toCritical(
        source: DamageSource
    ): DamageSource

    fun toKnownCause(
        source: DamageSource,
        cause: EntityDamageEvent.DamageCause
    ): DamageSource
}