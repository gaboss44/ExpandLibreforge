package com.github.gaboss44.expandlibreforge.extensions

import com.github.gaboss44.expandlibreforge.proxies.WorldAccessorProxy
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

private lateinit var worldAccessor: WorldAccessorProxy

object WorldExtensions {

    fun setProxyIfNeeded(proxy: WorldAccessorProxy) {
        if (!::worldAccessor.isInitialized) {
            worldAccessor = proxy
        }
    }
}

val World.arePlayerCritsDisabled get() = worldAccessor.arePlayerCritsDisabled(this)

val World.isSprintInterruptionOnAttackDisabled get() = worldAccessor.isSprintInterruptionOnAttackDisabled(this)

fun World.sendSmashAttackParticlesEvent(pos: Vector) {
    worldAccessor.sendSmashAttackParticles(this, pos)
}

fun World.sendSimpleDamageIndicatorParticles(target: Entity, amount: Int) {
    worldAccessor.sendSimpleDamageIndicatorParticles(this, target, amount)
}

val World.combatExhaustion get() = worldAccessor.getCombatExhaustion(this)