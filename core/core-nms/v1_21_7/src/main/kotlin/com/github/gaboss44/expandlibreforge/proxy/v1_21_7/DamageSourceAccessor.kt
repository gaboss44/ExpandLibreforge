package com.github.gaboss44.expandlibreforge.proxy.v1_21_7

import com.github.gaboss44.expandlibreforge.proxies.DamageSourceAccessorProxy
import com.github.gaboss44.expandlibreforge.proxy.common.toNMS
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.damage.DamageSource
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
class DamageSourceAccessor : DamageSourceAccessorProxy {

    override fun getWeapon(source: DamageSource): ItemStack? {
        return source.toNMS().weaponItem?.asBukkitMirror()
    }

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