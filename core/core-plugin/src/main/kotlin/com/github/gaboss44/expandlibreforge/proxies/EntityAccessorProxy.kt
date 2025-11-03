package com.github.gaboss44.expandlibreforge.proxies

import io.papermc.paper.event.entity.EntityKnockbackEvent
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.World
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExhaustionEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

@Suppress("UnstableApiUsage")
interface EntityAccessorProxy {

    fun getAttackDamage(entity: LivingEntity): Double

    fun getSpeed(entity: LivingEntity): Double

    fun getAttackKnockback(entity: LivingEntity): Double

    fun getKnockbackResistance(entity: LivingEntity): Double

    fun getSweepDamageRatio(entity: LivingEntity): Double

    fun hurtOrSimulate(
        target: Entity,
        source: DamageSource,
        damage: Float,
    ) : Boolean

    fun hurtServer(
        target: Entity,
        world: World,
        source: DamageSource,
        damage: Float,
    ) : Boolean

    fun knockback(
        target: LivingEntity,
        strength: Double,
        x: Double,
        z: Double
    )

    fun knockback(
        target: LivingEntity,
        strength: Double,
        x: Double,
        z: Double,
        attacker: Entity?,
        paperCause: EntityKnockbackEvent.Cause
    )

    fun knockbackWithoutResistance(
        target: LivingEntity,
        strength: Double,
        x: Double,
        z: Double,
        attacker: Entity?,
        paperCause: EntityKnockbackEvent.Cause
    )

    fun push(
        target: Entity,
        x: Double,
        y: Double,
        z: Double
    )

    fun push(
        target: Entity,
        x: Double,
        y: Double,
        z: Double,
        pusher: Entity?
    )

    fun getOnPos(entity: Entity): Vector

    fun sendSweepAttackEffects(player: Player)

    fun sendCriticalHitEffects(player: Player, entity: Entity)

    fun sendMagicalHitEffects(player: Player, entity: Entity)

    fun getLastHurtMob(entity: LivingEntity): Entity?

    fun setLastHurtMob(entity: LivingEntity, lastHurtMob: Entity)

    fun getLastHurtMobTimestamp(entity: LivingEntity): Int

    fun getLastDamageCancelled(entity: Entity): Boolean

    fun setLastDamageCancelled(entity: Entity, cancelled: Boolean)

    fun getHurtMarked(entity: Entity): Boolean

    fun setHurtMarked(entity: Entity, marked: Boolean)

    fun sendMotionPacket(player: Player, entity: Entity)

    fun stopSleeping(entity: LivingEntity)

    fun isInvulnerableToBase(entity: Entity, source: DamageSource): Boolean

    fun isAttackable(entity: Entity): Boolean

    fun areAllies(entity1: Entity, entity2: Entity): Boolean

    fun skipAttackInteraction(entity: Entity, attacker: Entity): Boolean

    fun getRiptideWeapon(entity: LivingEntity): ItemStack?

    fun getRiptideDamage(entity: LivingEntity): Float

    fun getLastDamageCause(entity: Entity): EntityDamageEvent?

    fun setLastDamageCause(entity: Entity, event: EntityDamageEvent?)

    fun getCurrentImpulseImpactPosition(player: Player): Vector?

    fun setCurrentImpulseImpactPosition(player: Player, position: Vector?)

    fun isIgnoringFallDamageFromCurrentImpulse(player: Player): Boolean

    fun setIgnoreFallDamageFromCurrentImpulse(player: Player, ignore: Boolean)

    fun setSpawnExtraParticlesOnFall(player: Player, spawn: Boolean)

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

    fun awardDamageDealt(player: Player, amount: Int)

    fun causeFoodExhaustion(player: Player, exhaustion: Float, reason: EntityExhaustionEvent.ExhaustionReason?)
}