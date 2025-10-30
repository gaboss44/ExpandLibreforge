package com.github.gaboss44.expandlibreforge.proxy.v1_21_4

import com.github.gaboss44.expandlibreforge.proxies.DamageSourceAccessorProxy
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.damage.DamageSource

@Suppress("UnstableApiUsage")
class DamageSourceAccessor : DamageSourceAccessorProxy {

    override fun toCritical(
        source: DamageSource
    ): DamageSource {
        val nmsSource = (source as CraftDamageSource).handle!!
        return CraftDamageSource(nmsSource.critical())
    }
}