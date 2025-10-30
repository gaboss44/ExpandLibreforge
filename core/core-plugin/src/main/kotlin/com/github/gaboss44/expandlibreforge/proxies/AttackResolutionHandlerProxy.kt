package com.github.gaboss44.expandlibreforge.proxies

import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface AttackResolutionHandlerProxy {

    fun hurtOrSimulate(
        target: Entity,
        source: DamageSource,
        damage: Float,
    ) : Boolean

    fun resolveAttack(
        attacker: Player,
        target: Entity,
        weapon: ItemStack,
        damageSource: DamageSource,
        baseDamage: Float,
        enchantedDamage: Float,
        attackStrengthScale: Float,
        didStrongAttack: Boolean,
        didCriticalAttack: Boolean,
        didSprintAttack: Boolean,
        didSweepAttack: Boolean,
        strongAttackSoundEffects: () -> Unit,
        weakAttackSoundEffects: () -> Unit,
        criticalAttackSoundEffects: () -> Unit,
        sweepAttackSoundEffects: () -> Unit,
        enchantedKnockbackEffects: (Entity, DamageSource, Float) -> Float,
        enchantedSweepDamageEffects: (Entity, DamageSource, Float) -> Float,
        onPostWeaponAttack: (Entity, DamageSource) -> Unit,
        onPreSweepAttack: (Entity, DamageSource) -> Boolean,
        onPostSweepAttack: (Entity, DamageSource) -> Unit,
    )
}