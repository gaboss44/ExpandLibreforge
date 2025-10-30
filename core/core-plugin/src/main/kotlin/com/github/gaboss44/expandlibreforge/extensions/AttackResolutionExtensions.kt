@file:Suppress("UnstableApiUsage")
package com.github.gaboss44.expandlibreforge.extensions

import com.github.gaboss44.expandlibreforge.proxies.AttackResolutionHandlerProxy
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

private lateinit var attackResolutionHandler: AttackResolutionHandlerProxy

object AttackResolutionExtensions {

    fun setProxyIfNeeded(proxy: AttackResolutionHandlerProxy) {
        if (!::attackResolutionHandler.isInitialized) {
            attackResolutionHandler = proxy
        }
    }
}

fun Entity.hurtOrSimulate(
    source: DamageSource,
    damage: Float,
) : Boolean {
    return attackResolutionHandler.hurtOrSimulate(this, source, damage)
}

fun Player.resolveAttack(
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
) {
    attackResolutionHandler.resolveAttack(
        attacker = this,
        target = target,
        weapon = weapon,
        damageSource = damageSource,
        baseDamage = baseDamage,
        enchantedDamage = enchantedDamage,
        attackStrengthScale = attackStrengthScale,
        didStrongAttack = didStrongAttack,
        didCriticalAttack = didCriticalAttack,
        didSprintAttack = didSprintAttack,
        didSweepAttack = didSweepAttack,
        strongAttackSoundEffects = strongAttackSoundEffects,
        weakAttackSoundEffects = weakAttackSoundEffects,
        criticalAttackSoundEffects = criticalAttackSoundEffects,
        sweepAttackSoundEffects = sweepAttackSoundEffects,
        enchantedKnockbackEffects = enchantedKnockbackEffects,
        enchantedSweepDamageEffects = enchantedSweepDamageEffects,
        onPostWeaponAttack = onPostWeaponAttack,
        onPreSweepAttack = onPreSweepAttack,
        onPostSweepAttack = onPostSweepAttack
    )
}