package com.github.gaboss44.expandlibreforge.proxy.v1_21_8

import com.github.gaboss44.expandlibreforge.proxies.DamageSourceAccessorProxy
import com.github.gaboss44.expandlibreforge.proxy.common.toNMS
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.damage.DamageSource
import org.bukkit.event.entity.EntityDamageEvent

@Suppress("UnstableApiUsage")
class DamageSourceAccessor : DamageSourceAccessorProxy {

    override fun toCritical(
        source: DamageSource
    ): DamageSource {
        return CraftDamageSource(source.toNMS().critical())
    }

    override fun toKnownCause(
        source: DamageSource,
        cause: EntityDamageEvent.DamageCause
    ): DamageSource {
        return CraftDamageSource(source.toNMS().knownCause(cause))
    }
}