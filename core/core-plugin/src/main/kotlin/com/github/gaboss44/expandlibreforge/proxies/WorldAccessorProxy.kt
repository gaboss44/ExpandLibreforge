package com.github.gaboss44.expandlibreforge.proxies

import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

interface WorldAccessorProxy {

    fun arePlayerCritsDisabled(world: World): Boolean

    fun isSprintInterruptionOnAttackDisabled(world: World): Boolean

    fun sendSmashAttackParticles(world: World, vector: Vector)

    fun sendSimpleDamageIndicatorParticles(world: World, target: Entity, amount: Int)

    fun getCombatExhaustion(world: World): Float
}