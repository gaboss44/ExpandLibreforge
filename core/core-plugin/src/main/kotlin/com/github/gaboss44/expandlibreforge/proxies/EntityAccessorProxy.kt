package com.github.gaboss44.expandlibreforge.proxies

import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack

interface EntityAccessorProxy {

    fun getAttackDamage(entity: LivingEntity): Double

    fun getSpeed(entity: LivingEntity): Double

    fun isAttackable(entity: Entity): Boolean

    fun skipAttackInteraction(entity: Entity, attacker: Entity): Boolean

    fun getRiptideWeapon(entity: LivingEntity): ItemStack?

    fun getRiptideDamage(entity: LivingEntity): Float

    fun getLastDamageCause(entity: Entity): EntityDamageEvent?

    fun setLastDamageCause(entity: Entity, event: EntityDamageEvent?)

    @Suppress("UnstableApiUsage")
    fun handleRedirectableProjectile(
        attacker: LivingEntity,
        target: Entity,
        source: DamageSource,
        damage: Float
    ): Boolean

    fun sendSoundEffect(
        fromEntity: Player,
        x: Double, y: Double, z: Double,
        sound: Sound,
        category: SoundCategory,
        volume: Float, pitch: Float
    )
}