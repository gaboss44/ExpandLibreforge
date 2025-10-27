package com.github.gaboss44.expandlibreforge.util

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import com.github.gaboss44.expandlibreforge.features.combo.Combo
import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData
import io.papermc.paper.event.entity.EntityDamageArmorAbsorbEvent
import io.papermc.paper.event.entity.EntityDamageMagicAbsorbEvent
import io.papermc.paper.event.entity.EntityEnchantedItemEffectsWithDamageSourceEvent
import io.papermc.paper.event.player.PlayerShieldDisableEvent
import org.bukkit.SoundCategory
import org.bukkit.damage.DamageSource
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent

const val floatHalfMaxValue = Float.MAX_VALUE / 2

fun EntityDamageByEntityEvent.tryDamagerAsPlayer(): Player? {
    return when (val damager = this.damager) {
        is Player -> damager
        is Projectile -> damager.shooter as? Player
        else -> if (Prerequisite.HAS_1_20_5.isMet) {
            damageSource.causingEntity as? Player ?:
            damageSource.directEntity as? Player
        } else null
    }
}

fun EntityDamageByEntityEvent.tryDamagerAsLivingEntity(): LivingEntity? {
    return when (val damager = this.damager) {
        is LivingEntity -> damager
        is Projectile -> damager.shooter as? LivingEntity
        else -> if (Prerequisite.HAS_1_20_5.isMet) {
            damageSource.causingEntity as? LivingEntity ?:
            damageSource.directEntity as? LivingEntity
        } else null
    }
}

fun EntityDamageByEntityEvent.tryDamagerAsProjectile(): Projectile? {
    return when (val damager = this.damager) {
        is Projectile -> damager
        else -> if (Prerequisite.HAS_1_20_5.isMet) {
            damageSource.directEntity as? Projectile
        } else null
    }
}

fun PlayerShieldDisableEvent.tryDamagerAsPlayer(): Player? {
    return when (val damager = this.damager) {
        is Player -> damager
        is Projectile -> damager.shooter as? Player
        else -> null
    }
}

fun PlayerShieldDisableEvent.tryDamagerAsLivingEntity(): LivingEntity? {
    return when (val damager = this.damager) {
        is LivingEntity -> damager
        is Projectile -> damager.shooter as? LivingEntity
        else -> null
    }
}

fun PlayerShieldDisableEvent.tryDamagerAsProjectile(): Projectile? {
    return when (val damager = this.damager) {
        is Projectile -> damager
        else -> null
    }
}

fun EntityKnockbackByEntityEvent.tryDamagerAsPlayer(): Player? {
    return when (val damager = this.hitBy) {
        is Player -> damager
        is Projectile -> damager.shooter as? Player
        else -> null
    }
}

fun EntityKnockbackByEntityEvent.tryDamagerAsLivingEntity(): LivingEntity? {
    return when (val damager = this.hitBy) {
        is LivingEntity -> damager
        is Projectile -> damager.shooter as? LivingEntity
        else -> null
    }
}

fun EntityKnockbackByEntityEvent.tryDamagerAsProjectile(): Projectile? {
    return when (val damager = this.hitBy) {
        is Projectile -> damager
        else -> null
    }
}

@Suppress("DEPRECATION")
fun Config.getDamageModifierEnum(key: String): EntityDamageEvent.DamageModifier? {
    val string = this.getStringOrNull(key) ?: return null
    return try {
        EntityDamageEvent.DamageModifier.valueOf(string.uppercase())
    } catch (_: IllegalArgumentException) {
        null
    }
}

fun Config.check(combo: Combo, data: TriggerData?): Boolean {
    fun Config.getOptionalInt(key: String): Int? =
        if (this.has(key)) this.getIntFromExpression(key, data) else null

    fun Config.getOptionalDouble(key: String): Double? =
        if (this.has(key)) this.getDoubleFromExpression(key, data) else null

    this.getOptionalInt("count_equals")?.let { if (combo.count != it) return false }
    this.getOptionalInt("count_greater_than")?.let { if (combo.count <= it) return false }
    this.getOptionalInt("count_lower_than")?.let { if (combo.count >= it) return false }
    this.getOptionalInt("count_at_least")?.let { if (combo.count < it) return false }
    this.getOptionalInt("count_at_most")?.let { if (combo.count > it) return false }

    this.getOptionalInt("remaining_ticks_equals")?.let { if (combo.remainingTicks != it) return false }
    this.getOptionalInt("remaining_ticks_greater_than")?.let { if (combo.remainingTicks <= it) return false }
    this.getOptionalInt("remaining_ticks_lower_than")?.let { if (combo.remainingTicks >= it) return false }
    this.getOptionalInt("remaining_ticks_at_least")?.let { if (combo.remainingTicks < it) return false }
    this.getOptionalInt("remaining_ticks_at_most")?.let { if (combo.remainingTicks > it) return false }

    this.getOptionalDouble("score_equals")?.let { if (combo.score != it) return false }
    this.getOptionalDouble("score_greater_than")?.let { if (combo.score <= it) return false }
    this.getOptionalDouble("score_lower_than")?.let { if (combo.score >= it) return false }
    this.getOptionalDouble("score_at_least")?.let { if (combo.score < it) return false }
    this.getOptionalDouble("score_at_most")?.let { if (combo.score > it) return false }

    return true
}

fun Config.check(combo: Combo, player: Player): Boolean {
    fun Config.getOptionalInt(key: String): Int? =
        if (this.has(key)) this.getIntFromExpression(key, player) else null

    fun Config.getOptionalDouble(key: String): Double? =
        if (this.has(key)) this.getDoubleFromExpression(key, player) else null

    this.getOptionalInt("count_equals")?.let { if (combo.count != it) return false }
    this.getOptionalInt("count_greater_than")?.let { if (combo.count <= it) return false }
    this.getOptionalInt("count_lower_than")?.let { if (combo.count >= it) return false }
    this.getOptionalInt("count_at_least")?.let { if (combo.count < it) return false }
    this.getOptionalInt("count_at_most")?.let { if (combo.count > it) return false }

    this.getOptionalInt("remaining_ticks_equals")?.let { if (combo.remainingTicks != it) return false }
    this.getOptionalInt("remaining_ticks_greater_than")?.let { if (combo.remainingTicks <= it) return false }
    this.getOptionalInt("remaining_ticks_lower_than")?.let { if (combo.remainingTicks >= it) return false }
    this.getOptionalInt("remaining_ticks_at_least")?.let { if (combo.remainingTicks < it) return false }
    this.getOptionalInt("remaining_ticks_at_most")?.let { if (combo.remainingTicks > it) return false }

    this.getOptionalDouble("score_equals")?.let { if (combo.score != it) return false }
    this.getOptionalDouble("score_greater_than")?.let { if (combo.score <= it) return false }
    this.getOptionalDouble("score_lower_than")?.let { if (combo.score >= it) return false }
    this.getOptionalDouble("score_at_least")?.let { if (combo.score < it) return false }
    this.getOptionalDouble("score_at_most")?.let { if (combo.score > it) return false }

    return true
}

fun getSoundCategoryOrElse(string: String?, default: SoundCategory): SoundCategory {
    return try {
        if (string == null) {
            default
        } else {
            SoundCategory.valueOf(string.uppercase())
        }
    } catch (_: IllegalArgumentException) {
        default
    }
}

fun getEventResult(string: String?): Event.Result? {
    if (string == null) return null
    return try {
        Event.Result.valueOf(string.uppercase())
    } catch (_: IllegalArgumentException) { null }
}

val Event.damageSource : DamageSource?
    get() {
        return when (this) {
            is EntityDamageArmorAbsorbEvent -> this.damageSource
            is EntityDamageMagicAbsorbEvent -> this.damageSource
            is EntityDamageByEntityEvent -> this.damageSource
            is EntityDamageEvent -> this.damageSource
            is EntityDeathEvent -> this.damageSource
            is EntityEnchantedItemEffectsWithDamageSourceEvent -> this.damageSource
            else -> {
                try {
                    val clazz = this::class.java
                    val m = clazz.getMethod("getDamageSource")
                    val res = m.invoke(this)
                    if (res is DamageSource) {
                        return res
                    }
                } catch (_: Exception) { }

                try {
                    val f = this::class.java.getDeclaredField("damageSource")
                    f.isAccessible = true
                    val res = f.get(this)
                    if (res is DamageSource) return res
                } catch (_: Exception) { }

                return null
            }
        }
    }
