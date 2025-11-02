@file:Suppress("UnstableApiUsage")

package com.github.gaboss44.expandlibreforge.extensions

import com.github.gaboss44.expandlibreforge.events.entity.EntityCriticalCheckEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentDamageEffectsEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentFallBasedDamageEffectsEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentKnockbackEffectsEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentPostAttackBySlotEffectsEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentPostAttackByWeaponEffectsEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntitySmashCheckEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntitySmashFallDistanceEvent
import com.github.gaboss44.expandlibreforge.prerequisites.PrerequisiteHasSmashAttemptEvent
import com.github.gaboss44.expandlibreforge.proxies.EntityAccessorProxy
import com.github.gaboss44.expandlibreforge.util.getBoolOrElse
import com.willfp.eco.core.Prerequisite
import io.papermc.paper.event.entity.EntityAttemptSmashAttackEvent
import io.papermc.paper.event.entity.EntityKnockbackEvent
import org.apache.commons.lang3.mutable.MutableFloat
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.Tag
import org.bukkit.World
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.ComplexEntityPart
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Tameable
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExhaustionEvent
import org.bukkit.event.player.PlayerVelocityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.UUID
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.math.roundToInt

private lateinit var entityAccessor: EntityAccessorProxy

object EntityExtensions {

    fun setProxyIfNeeded(proxy: EntityAccessorProxy) {
        if (!::entityAccessor.isInitialized) {
            entityAccessor = proxy
        }
    }
}

val LivingEntity.canCrit get() = this.fallDistance > 0.0 && !this.isOnGround && !this.isClimbing && !this.isInWater && !this.hasPotionEffect(PotionEffectType.BLINDNESS) && !this.isInsideVehicle && !(this is Player && this.isSprinting)

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

val LivingEntity.attackKnockback get() = entityAccessor.getAttackKnockback(this)

val LivingEntity.knockbackResistance get() = entityAccessor.getKnockbackResistance(this)

val LivingEntity.sweepDamageRatio get() = entityAccessor.getSweepDamageRatio(this)

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

fun Entity.isAlliedTo(other: Entity) = entityAccessor.areAllies(this, other)

fun LivingEntity.handleRedirectableProjectile(
    target: Entity,
    source: DamageSource,
    damage: Float
) = entityAccessor.handleRedirectableProjectile(this, target, source, damage)

fun Entity.hurtOrSimulate(
    source: DamageSource,
    damage: Float
) = entityAccessor.hurtOrSimulate(this, source, damage)

fun Entity.hurtServer(
    world: World,
    source: DamageSource,
    damage: Float,
) : Boolean = entityAccessor.hurtServer(this, world, source, damage)

fun LivingEntity.applyKnockback(
    strength: Double,
    x: Double,
    z: Double
) { entityAccessor.knockback(this, strength, x, z) }

fun LivingEntity.applyKnockback(
    strength: Double,
    x: Double,
    y: Double,
    attacker: Entity?,
    paperCause: EntityKnockbackEvent.Cause
) { entityAccessor.knockback(this, strength, x, y, attacker, paperCause) }

fun LivingEntity.applyKnockbackWithoutResistance(
    strength: Double,
    x: Double,
    z: Double,
    attacker: Entity?,
    paperCause: EntityKnockbackEvent.Cause
) { entityAccessor.knockbackWithoutResistance(this, strength, x, z, attacker, paperCause) }

fun Entity.push(
    x: Double,
    y: Double,
    z: Double
) = entityAccessor.push(this, x, y, z)

fun Entity.push(
    x: Double,
    y: Double,
    z: Double,
    pusher: Entity?
) = entityAccessor.push(this, x, y, z, pusher)

fun Player.sendMotionPacket(entity: Entity) =
    entityAccessor.sendMotionPacket(this, entity)

fun Player.sendSweepAttackEffects() =
    entityAccessor.sendSweepAttackEffects(this)

fun Player.sendCriticalHitEffects(target: Entity) =
    entityAccessor.sendCriticalHitEffects(this, target)

fun Player.sendMagicalHitEffects(target: Entity) =
    entityAccessor.sendMagicalHitEffects(this, target)

fun Player.awardDamageDealt(amount: Int) =
    entityAccessor.awardDamageDealt(this, amount)

var Entity.lastDamageCancelled
    get() = entityAccessor.getLastDamageCancelled(this)
    set(value) = entityAccessor.setLastDamageCancelled(this, value)

var Entity.hurtMarked
    get() = entityAccessor.getHurtMarked(this)
    set(value) = entityAccessor.setHurtMarked(this, value)

var LivingEntity.lastHurtMob
    get() = entityAccessor.getLastHurtMob(this)
    set(value) {
        if (value == null) return
        entityAccessor.setLastHurtMob(this, value)
    }

var Player.currentImpulseImpactPosition: Vector?
    get() = entityAccessor.getCurrentImpulseImpactPosition(this)
    set(value) = entityAccessor.setCurrentImpulseImpactPosition(this, value)

var Player.ignoresFallDamageFromCurrentImpulse: Boolean
    get() = entityAccessor.isIgnoringFallDamageFromCurrentImpulse(this)
    set(value) = entityAccessor.setIgnoreFallDamageFromCurrentImpulse(this, value)

fun Player.calculateImpactPosition(): Vector {
    if (!this.ignoresFallDamageFromCurrentImpulse) return this.location.toVector()
    val currentImpulseImpactPosition = this.currentImpulseImpactPosition ?: return this.location.toVector()
    if (currentImpulseImpactPosition.y < this.location.y) return this.location.toVector()
    return currentImpulseImpactPosition
}

fun Player.setSpawnExtraParticlesOnFall(value: Boolean) =
    entityAccessor.setSpawnExtraParticlesOnFall(this, value)

fun Player.causeFoodExhaustion(exhaustion: Float, reason: EntityExhaustionEvent.ExhaustionReason?) {
    entityAccessor.causeFoodExhaustion(this, exhaustion, reason)
}

val Entity.onPos get() = entityAccessor.getOnPos(this)

fun LivingEntity.stopSleeping() = entityAccessor.stopSleeping(this)

fun Entity.isInvulnerableToBase(
    source: DamageSource
) = entityAccessor.isInvulnerableToBase(this, source)

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

/**
    Returns a DamageSource.Builder for either a direct attack or a smash attack,
    depending on the weapon type and whether the smash attack conditions are met.
 */
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

fun getSmashAttackKnockbackPredicate(attacker: Entity, target: Entity) = Predicate { entity: Entity ->
    if (entity !is LivingEntity) false
    if (entity is Player && entity.gameMode == GameMode.SPECTATOR) false
    if (entity == attacker) false
    if (entity == target) false
    if (attacker.isAlliedTo(entity)) false
    if (entity is Tameable && target is LivingEntity && entity.isTamed && entity.owner == target) false
    if (target.location.toVector().distanceSquared(entity.location.toVector()) > 12.25) false
    true
}

val LivingEntity.nullableActiveItem: ItemStack? get() =
    if (this.activeItem.isEmpty) null else this.activeItem

private val performingAttacks = mutableSetOf<UUID>()

var Player.isPerformingAttack
    get() = this.uniqueId in performingAttacks
    private set(value) {
        if (value) {
            performingAttacks.add(this.uniqueId)
        } else {
            performingAttacks.remove(this.uniqueId)
        }
    }

fun Player.performAttackSafely(
    target: Entity,
    damage: Float? = null,
    slot: EquipmentSlot? = null,
    source: DamageSource? = null
) {
    if (this.isPerformingAttack) return

    try {
        this.isPerformingAttack = true
        this.performAttack(target, damage, slot, source)
    } finally {
        this.isPerformingAttack = false
    }
}

/**
    Performs an attack from this player to the target entity.
    This method handles damage calculation, critical hits, knockback, and various attack effects.
    Major parts of the logic are copied from PaperMC's Player#attack method,
    with modifications to allow for richer event handling and customization.
 */
fun Player.performAttack(
    target: Entity,
    damage: Float? = null,
    slot: EquipmentSlot? = null,
    source: DamageSource? = null
) {
    if (!target.isAttackable || target.skipsAttackInteractionFrom(this)) {
        return
    }

    var damage = damage ?: this.currentAttackDamage.toFloat()

    val weapon = slot?.let { this.inventory.getItem(it) } ?: this.weapon ?: ItemStack.empty()

    // Set slot after finding a weapon because of a possible riptide attack
    val slot = slot ?: EquipmentSlot.HAND

    var source = source ?: this.getDirectOrSmashAttackDamageSource(
        defaultDamageType = DamageType.PLAYER_ATTACK,
        target = target,
        weapon = weapon
    ).withDamageLocation(target.location).build()

    val smashAttack = source.damageType == DamageType.MACE_SMASH

    val mutableEnchantedDamage = MutableFloat(damage)
    EnchantmentHelpers.modifyDamage(mutableEnchantedDamage, this.world, target, weapon, source) {
        EntityEnchantmentDamageEffectsEvent(
            this, it, target, mutableEnchantedDamage, damage, weapon, source
        ).callEvent()
    }
    var enchantedDamage = mutableEnchantedDamage.value - damage

    val attackStrengthScale = this.attackCooldown

    damage *= 0.2f + attackStrengthScale * attackStrengthScale * 0.8f
    enchantedDamage *= attackStrengthScale

    // Handle redirectable projectiles such as fireballs
    if (this.handleRedirectableProjectile(target, source, enchantedDamage)) return

    // Cancel if no damage to deal
    if (damage < 0.0f && enchantedDamage < 0.0f) return

    /*
        Strong attack and sprint attack check
     */
    val strongAttack = attackStrengthScale > 0.9f
    val sprintAttack = if (this.isSprinting && strongAttack) {
        this.sendSoundEffect(this.x, this.y, this.z, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1.0f, 1.0f)
        true
    } else {
        false
    }

    /*
        Smash attack fall distance based damage
     */
    var smashFallDistance = 0.0f
    if (smashAttack) {
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
        smashFallDistance = smashFallDistanceEvent.overrideFallDistance

        val mutableFallBasedDamage = MutableFloat(0f)
        EnchantmentHelpers.modifyFallBasedDamage(mutableFallBasedDamage, this.world, target, weapon, source) {
            EntityEnchantmentFallBasedDamageEffectsEvent(
                this, it, target, mutableFallBasedDamage, 0f, weapon, source
            ).callEvent()
        }

        damage += mutableFallBasedDamage.value * smashFallDistance
    }

    /*
        Critical hit check
     */
    val critsDisabled = this.world.arePlayerCritsDisabled
    val initialCriticalAttack = strongAttack && this.canCrit && target is LivingEntity && !critsDisabled

    val mutableCriticalMultiplier = MutableFloat(1.5f)
    val criticalCheckEvent = EntityCriticalCheckEvent(this, target, weapon, source, mutableCriticalMultiplier, critsDisabled, initialCriticalAttack)
    criticalCheckEvent.callEvent()
    val criticalAttack = criticalCheckEvent.result.getBoolOrElse(initialCriticalAttack)

    // Apply critical hit modifications
    if (criticalAttack) {
        source = source.toCritical()
        damage *= mutableCriticalMultiplier.value
    }

    // Total damage after all modifications
    val damage2 = damage + enchantedDamage

    /*
        Sweep attack check
     */
    var sweepAttack = false
    if (strongAttack && !criticalAttack && !sprintAttack && this.isOnGround) {
        val horizontal = this.knownVelocity.let { it.x * it.x + it.z * it.z}
        val speedFactor = this.speed * 2.5
        if (horizontal < speedFactor * speedFactor && Tag.ITEMS_SWORDS.isTagged(weapon.type)) {
            sweepAttack = true
        }
    }

    val targetVelocity = target.velocity
    val targetHealth = if (target is LivingEntity) target.health else 0.0

    // Apply damage and return if not successful
    if (!target.hurtOrSimulate(source, damage + enchantedDamage)) {
        this.sendSoundEffect(this.x, this.y, this.z, Sound.ENTITY_PLAYER_ATTACK_NODAMAGE, SoundCategory.PLAYERS, 1.0f, 1.0f)
        return
    }

    /*
        Knockback application
     */
    var knockback = this.attackKnockback.toFloat()
    val mutableKnockback = MutableFloat(knockback)
    EnchantmentHelpers.modifyKnockback(mutableKnockback, this.world, target, weapon, source) {
        EntityEnchantmentKnockbackEffectsEvent(
            this, it, target, mutableKnockback, knockback, weapon, source
        ).callEvent()
    }
    knockback = mutableKnockback.value + if (sprintAttack) 1.0f else 0.0f
    if (knockback > 0.0f) {
        if (target is LivingEntity) {
            if (Prerequisite.HAS_PAPER.isMet) {
                val targetKnockbackResistance = target.knockbackResistance.coerceAtLeast(0.0)
                target.applyKnockbackWithoutResistance(
                    knockback * 0.5 / (1.0 + targetKnockbackResistance),
                    sin(this.yaw * (Math.PI / 180.0).toFloat()).toDouble(),
                    -cos(this.yaw * (Math.PI / 180.0).toFloat()).toDouble(),
                    this, EntityKnockbackEvent.Cause.ENTITY_ATTACK
                )
            } else {
                target.applyKnockback(
                    knockback * 0.5,
                    sin(this.yaw * (Math.PI / 180.0).toFloat()).toDouble(),
                    -cos(this.yaw * (Math.PI / 180.0).toFloat()).toDouble()
                )
            }
        } else {
            if (Prerequisite.HAS_PAPER.isMet) {
                target.push(
                    -sin(this.yaw * (Math.PI / 180.0).toFloat()).toDouble() * knockback * 0.5,
                    0.1,
                    cos(this.yaw * (Math.PI / 180.0).toFloat()).toDouble() * knockback * 0.5,
                    this
                )
            } else {
                target.push(
                    -sin((this.yaw * (Math.PI / 180.0)).toFloat()).toDouble() * knockback * 0.5,
                    0.1,
                    cos(((this.yaw * Math.PI / 180.0).toFloat())).toDouble() * knockback * 0.5
                )
            }
        }

        this.velocity = this.velocity.apply {
            x *= 0.6
            z *= 0.6
        }
        if (!this.world.isSprintInterruptionOnAttackDisabled) {
            this.isSprinting = false
        }
    }

    if (sweepAttack) {
        val sweepDamage = 1.0f + this.sweepDamageRatio.toFloat() * damage
        val sweepSource = source.toKnownCause(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)

        val entityFilter = { entity: Entity -> entity is LivingEntity &&
                entity != this &&
                entity != target &&
                !this.isAlliedTo(entity) &&
                !(entity is ArmorStand && entity.isMarker) &&
                entity.location.distanceSquared(target.location) < 9.0
        }
        for (entity in this.world.getNearbyEntities(this.boundingBox.expand(1.0, 0.25, 1.0), entityFilter)) {
            entity as? LivingEntity ?: continue
            val mutableEnchantedSweepDamage = MutableFloat(sweepDamage)
            EnchantmentHelpers.modifyDamage(mutableEnchantedSweepDamage, this.world, entity, weapon, sweepSource) {
                EntityEnchantmentDamageEffectsEvent(
                    this, it, entity, mutableEnchantedSweepDamage, sweepDamage, weapon, sweepSource
                ).callEvent()
            }
            val enchantedSweepDamage = mutableEnchantedSweepDamage.value * attackStrengthScale
            entity.lastDamageCancelled = false
            if (!entity.hurtServer(this.world, sweepSource, enchantedSweepDamage) || !entity.lastDamageCancelled) continue
            if (Prerequisite.HAS_PAPER.isMet) {
                val entityKnockbackResistance = entity.knockbackResistance.coerceAtLeast(0.0)
                entity.applyKnockbackWithoutResistance(
                    0.4 / (1.0 + entityKnockbackResistance),
                    sin((this.yaw * (Math.PI / 180.0)).toFloat()).toDouble(),
                    -cos((this.yaw * Math.PI / 180.0).toFloat()).toDouble(),
                    this, EntityKnockbackEvent.Cause.SWEEP_ATTACK
                )
            } else {
                entity.applyKnockback(
                    0.4,
                    sin((this.yaw * (Math.PI / 180.0)).toFloat()).toDouble(),
                    -cos((this.yaw * Math.PI / 180.0).toFloat()).toDouble()
                )
            }

            val postAttackByWeaponConsumer = Consumer { data: EnchantmentEffectsData ->
                EntityEnchantmentPostAttackByWeaponEffectsEvent(
                    this, data, target, weapon, source
                ).callEvent()
            }
            val postAttackBySlotConsumer = Consumer { data: EnchantmentEffectsInSlotData ->
                EntityEnchantmentPostAttackBySlotEffectsEvent(
                    entity, data, this, source
                ).callEvent()
            }
            EnchantmentHelpers.doPostAttackEffects(
                this.world, entity, weapon, sweepSource,
                listOf(postAttackByWeaponConsumer),
                listOf(postAttackBySlotConsumer)
            )
        }

        this.sendSoundEffect(this.x, this.y, this.z, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0f, 1.0f)
        this.sendSweepAttackEffects()
    }

    if (target is Player && target.hurtMarked) {
        var cancelled = false
        val velocity2 = target.velocity
        val velocityEvent = PlayerVelocityEvent(target, velocity2)
        velocityEvent.callEvent()

        if (velocityEvent.isCancelled) {
            cancelled = true
        } else if (velocity2 != velocityEvent.velocity) {
            target.velocity = velocityEvent.velocity
        }

        if (!cancelled) {
            target.sendMotionPacket(target)
            target.velocity = targetVelocity
            target.hurtMarked = false
        }
    }

    if (criticalAttack) {
        this.sendSoundEffect(
            this.x, this.y, this.z,
            Sound.ENTITY_PLAYER_ATTACK_CRIT,
            SoundCategory.PLAYERS,
            1.0f, 1.0f
        )
        this.sendCriticalHitEffects(target)
    }

    if (!criticalAttack && !sweepAttack) {
        if (strongAttack) {
            this.sendSoundEffect(
                this.x, this.y, this.z,
                Sound.ENTITY_PLAYER_ATTACK_STRONG,
                SoundCategory.PLAYERS,
                1.0f, 1.0f
            )
        } else {
            this.sendSoundEffect(
                this.x, this.y, this.z,
                Sound.ENTITY_PLAYER_ATTACK_WEAK,
                SoundCategory.PLAYERS,
                1.0f, 1.0f
            )
        }
    }

    if (enchantedDamage > 0.0f) {
        this.sendMagicalHitEffects(target)
    }

    this.lastHurtMob = target

    val parentTarget = if (target is ComplexEntityPart) target.parent else target

    var didHurt = false
    val isWeapon = weapon.isWeapon

    if (parentTarget is LivingEntity) {
        if (isWeapon) {
            weapon.awardUsage(this)
            didHurt = true
        }

        if (smashAttack) {
            this.velocity = this.velocity.apply { y = 0.01 }
            this.currentImpulseImpactPosition = this.calculateImpactPosition()
            this.ignoresFallDamageFromCurrentImpulse = true
            this.sendMotionPacket(this)


            val onGround = parentTarget.isOnGround
            val sound = when {
                onGround && smashFallDistance > 5.0f ->
                    Sound.ITEM_MACE_SMASH_GROUND_HEAVY
                onGround ->
                    Sound.ITEM_MACE_SMASH_GROUND
                else ->
                    Sound.ITEM_MACE_SMASH_AIR
            }
            if (onGround) {
                this.setSpawnExtraParticlesOnFall(true)
            }

            this.sendSoundEffect(this.x, this.y, this.z, sound, SoundCategory.PLAYERS, 1.0f, 1.0f)

            val nearby = this.world.getNearbyEntities(parentTarget.boundingBox.expand(3.5), getSmashAttackKnockbackPredicate(this, parentTarget))
            for (entity in nearby) {
                entity as? LivingEntity ?: continue
                val vec3 = entity.location.toVector().subtract(parentTarget.location.toVector())
                val f1 = (3.5 - vec3.length()) * 0.7f
                val f2 = if (smashFallDistance > 5.0f) 2.0f else 1.0f
                val f3 = 1 - entity.knockbackResistance
                if (f1 <= 0.0f || f3 <= 0.0f) continue
                val vec31 = vec3.normalize().multiply(f1 * f2 * f3)
                if (Prerequisite.HAS_PAPER.isMet) {
                    entity.push(
                        vec31.x,
                        0.7,
                        vec31.z,
                        this
                    )
                } else {
                    entity.push(
                        vec31.x,
                        0.7,
                        vec31.z
                    )
                }

                if (entity is Player) {
                    entity.sendMotionPacket(entity)
                }
            }
        }
    }

    /*
        Post-attack enchantment effects
     */

    val postAttackByWeaponEffectsConsumer = Consumer { data: EnchantmentEffectsData ->
        EntityEnchantmentPostAttackByWeaponEffectsEvent(
            this, data, target, weapon, source
        ).callEvent()
    }
    val postAttackBySlotEffectsConsumer = Consumer { data: EnchantmentEffectsInSlotData ->
        if (target !is LivingEntity) return@Consumer
        EntityEnchantmentPostAttackBySlotEffectsEvent(
            target, data, this, source
        ).callEvent()
    }

    EnchantmentHelpers.doPostAttackEffects(
        this.world, target, weapon, source,
        listOf(postAttackByWeaponEffectsConsumer),
        listOf(postAttackBySlotEffectsConsumer)
    )

    if (smashAttack) {
        this.fallDistance = 0f
    }

    if (!weapon.isEmpty && parentTarget is LivingEntity) {
        if (didHurt) {
            weapon.hurtAndBreak(weapon.itemDamagePerAttack, this, slot)
        }
        if (weapon.isEmpty && weapon == this.inventory.getItem(slot)) {
            this.inventory.setItem(slot, ItemStack.empty())
        }
    }

    if (target is LivingEntity) {
        val dealt = targetHealth - target.health
        this.awardDamageDealt((dealt * 10.0f).roundToInt())

        if (dealt > 2.0) {
            this.world.sendSimpleDamageIndicatorParticles(target, (dealt * 0.5).roundToInt())
        }
    }

    this.causeFoodExhaustion(this.world.combatExhaustion, EntityExhaustionEvent.ExhaustionReason.ATTACK)
}
