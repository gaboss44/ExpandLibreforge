@file:Suppress("UnstableApiUsage")

package com.github.gaboss44.expandlibreforge.extensions

import com.github.gaboss44.expandlibreforge.events.entity.EntityCriticalCheckEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentDamageEffectsEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentFallBasedDamageEffectsEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntitySmashCheckEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntitySmashFallDistanceEvent
import com.github.gaboss44.expandlibreforge.prerequisites.PrerequisiteHasSmashAttemptEvent
import com.github.gaboss44.expandlibreforge.proxies.EntityAccessorProxy
import com.github.gaboss44.expandlibreforge.util.getBoolOrElse
import io.papermc.paper.event.entity.EntityAttemptSmashAttackEvent
import org.apache.commons.lang3.mutable.MutableFloat
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.Tag
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

private lateinit var entityAccessor: EntityAccessorProxy

object EntityExtensions {

    fun setProxyIfNeeded(proxy: EntityAccessorProxy) {
        if (!::entityAccessor.isInitialized) {
            entityAccessor = proxy
        }
    }
}

val LivingEntity.canCrit get() = this.fallDistance > 0.0 && !this.isOnGround && !this.isClimbing && !this.isInWater && !this.hasPotionEffect(PotionEffectType.BLINDNESS) && !this.isInsideVehicle && (this !is Player || this.isSprinting)

fun Player.sendSoundEffect(
    x: Double, y: Double, z: Double,
    sound: Sound,
    category: SoundCategory,
    volume: Float, pitch: Float
) {
    entityAccessor.sendSoundEffect(this, x, y, z, sound, category, volume, pitch)
}

var Entity.lastDamageCause
    get() = entityAccessor.getLastDamageCause(this)
    set(value) = entityAccessor.setLastDamageCause(this, value)

val Entity.isAttackable get() = entityAccessor.isAttackable(this)

fun Entity.skipsAttackInteractionFrom(attacker: Entity) =
    entityAccessor.skipAttackInteraction(this, attacker)

val LivingEntity.weapon get() = if (isRiptiding) {
    this.riptideWeapon ?: this.equipment?.itemInMainHand
} else {
    this.equipment?.itemInMainHand
}

val LivingEntity.attackDamage get() = entityAccessor.getAttackDamage(this)

val LivingEntity.speed get() = entityAccessor.getSpeed(this)

val LivingEntity.currentAttackDamage : Number get() = if (isRiptiding) {
    this.riptideDamage
} else {
    this.attackDamage
}

val LivingEntity.riptideWeapon get() = entityAccessor.getRiptideWeapon(this)

val LivingEntity.riptideDamage get() = entityAccessor.getRiptideDamage(this)

val LivingEntity.canSmashAttack get() = this.fallDistance > 1.5 && !this.isGliding

fun Entity.getKnownVelocity(visited: MutableList<Entity>): Vector {
    if (visited.contains(this)) {
        return this.velocity
    } else {
        visited.add(this)
    }
    val controllingPassenger = this.passengers.firstOrNull()
    return if (controllingPassenger is Player && controllingPassenger.isValid) {
        controllingPassenger.getKnownVelocity(visited)
    } else {
        this.velocity
    }
}

val Entity.knownVelocity get() = this.getKnownVelocity(mutableListOf())

fun LivingEntity.handleRedirectableProjectile(
    target: Entity,
    source: DamageSource,
    damage: Float
) = entityAccessor.handleRedirectableProjectile(this, target, source, damage)


fun LivingEntity.attemptSmashAttack(
    target: LivingEntity,
    weapon: ItemStack
) : Boolean {
    val canSmashAttack = canSmashAttack
    return if (PrerequisiteHasSmashAttemptEvent.isMet) {
        val event = EntityAttemptSmashAttackEvent(this, target, weapon, canSmashAttack)
        event.callEvent()
        event.result.getBoolOrElse(canSmashAttack)
    } else {
        val event = EntitySmashCheckEvent(this, target, weapon, canSmashAttack)
        event.callEvent()
        event.result.getBoolOrElse(canSmashAttack)
    }
}

val LivingEntity.smashAttackDamageSource get() =
    DamageSource.builder(DamageType.MACE_SMASH)
        .withDirectEntity(this)
        .withCausingEntity(this)

val LivingEntity.playerAttackDamageSource get() =
    DamageSource.builder(DamageType.PLAYER_ATTACK)
        .withDirectEntity(this)
        .withCausingEntity(this)

fun LivingEntity.getDirectAttackDamageSource(damageType: DamageType) =
    DamageSource.builder(damageType)
        .withDirectEntity(this)
        .withCausingEntity(this)

fun LivingEntity.getDirectOrSmashAttackDamageSource(
    defaultDamageType: DamageType,
    target: Entity? = null,
    weapon: ItemStack = ItemStack.empty(),
    fireSmashEvent: Boolean = target != null
) : DamageSource.Builder {
    return if (weapon.type != Material.MACE) {
        this.getDirectAttackDamageSource(defaultDamageType)
    } else if (target == null) {
        this.getDirectAttackDamageSource(defaultDamageType)
    } else if (target !is LivingEntity) {
        this.getDirectAttackDamageSource(defaultDamageType)
    } else if (fireSmashEvent) {
        if (this.attemptSmashAttack(target, weapon)) {
            return this.smashAttackDamageSource
        } else {
            this.getDirectAttackDamageSource(defaultDamageType)
        }
    } else if (canSmashAttack) {
        return this.smashAttackDamageSource
    } else {
        this.getDirectAttackDamageSource(defaultDamageType)
    }
}

fun Player.performAttack(
    target: Entity,
    damage: Float? = null,
    weapon: ItemStack? = null,
    source: DamageSource? = null
) {
    if (!target.isAttackable || target.skipsAttackInteractionFrom(this)) {
        return
    }

    var damage = damage ?: this.currentAttackDamage.toFloat()

    val weapon = weapon ?: this.weapon ?: ItemStack.empty()

    var source = source ?: this.getDirectOrSmashAttackDamageSource(
        defaultDamageType = DamageType.PLAYER_ATTACK,
        target = target,
        weapon = weapon
    ).withDamageLocation(target.location).build()

    var enchantedDamage = this.modifyDamage(damage, target, weapon, source) { EntityEnchantmentDamageEffectsEvent(
        this, it, target, damage, weapon, source).callEvent()
    }.minus(damage)

    val attackStrengthScale = this.attackCooldown

    damage *= 0.2f + attackStrengthScale * attackStrengthScale * 0.8f
    enchantedDamage *= attackStrengthScale

    if (this.handleRedirectableProjectile(target, source, enchantedDamage)) return

    if (damage < 0.0f && enchantedDamage < 0.0f) return

    val strongAttack = attackStrengthScale > 0.9f
    val sprintAttack = if (this.isSprinting && strongAttack) {
        this.sendSoundEffect(this.x, this.y, this.z, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1.0f, 1.0f)
        true
    } else {
        false
    }

    if (source.damageType == DamageType.MACE_SMASH) {
        val smashFallDistanceEvent = EntitySmashFallDistanceEvent(
            attacker = this,
            target = target,
            weapon = weapon,
            source = source,
            fallDistance = fallDistance,
            ranges = listOf(4 to 3, 2 to 8, 1 to Double.MAX_VALUE)
        )
        smashFallDistanceEvent.callEvent()
        damage += smashFallDistanceEvent.finalFallBasedDamage

        val fallBasedDamage = MutableFloat(0f)
        this.modifyFallBasedDamage(fallBasedDamage, target, weapon, source) { EntityEnchantmentFallBasedDamageEffectsEvent(
            this, it, target, fallBasedDamage, 0f, weapon, source).callEvent()
        }

        damage += fallBasedDamage.value * smashFallDistanceEvent.overrideFallDistance
    }

    val critsDisabled = this.world.arePlayerCritsDisabled
    var criticalAttack = strongAttack && this.canCrit && target is LivingEntity && !critsDisabled

    val criticalCheckEvent = EntityCriticalCheckEvent(this, target, weapon, source, 1.5f, critsDisabled, criticalAttack)
    criticalCheckEvent.callEvent()
    criticalAttack = criticalCheckEvent.result.getBoolOrElse(criticalAttack)

    if (criticalAttack) {
        source = source.toCritical()
        damage *= criticalCheckEvent.criticalMultiplier
    }

    val damage2 = damage + enchantedDamage

    var sweepAttack = false
    if (strongAttack && !criticalAttack && !sprintAttack && this.isOnGround) {
        val horizontal = this.knownVelocity.let { it.x * it.x + it.z * it.z}
        val speedFactor = this.speed * 2.5
        if (horizontal < speedFactor * speedFactor && Tag.ITEMS_SWORDS.isTagged(weapon.type)) {
            sweepAttack = true
        }
    }
}
