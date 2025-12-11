@file:Suppress("UnstableApiUsage")

package com.github.gaboss44.expandlibreforge.util

import com.github.gaboss44.expandlibreforge.features.combo.Combo
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.EquipmentSlot
import java.util.function.BooleanSupplier

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

fun getEquipmentSlot(string: String?): EquipmentSlot? {
    if (string == null) return null
    return try {
        EquipmentSlot.valueOf(string.uppercase())
    } catch (_: IllegalArgumentException) { null }
}

fun getEventResult(string: String?): Event.Result? {
    if (string == null) return null
    return try {
        Event.Result.valueOf(string.uppercase())
    } catch (_: IllegalArgumentException) { null }
}

fun Event.Result.getBoolOrElse(supplier: BooleanSupplier) : Boolean {
    return when (this) {
        Event.Result.ALLOW -> true
        Event.Result.DENY -> false
        Event.Result.DEFAULT -> supplier.asBoolean
    }
}

fun Event.Result.getBoolOrElse(default: Boolean) : Boolean {
    return when (this) {
        Event.Result.ALLOW -> true
        Event.Result.DENY -> false
        Event.Result.DEFAULT -> default
    }
}

fun Double.scaledBy(vararg ranges: Pair<Number, Number>) = this.scaledBy(ranges.toList())

fun Double.scaledBy(ranges: List<Pair<Number, Number>>): Double {
    if (ranges.isEmpty()) return this

    var accumulated = 0.0
    var lastLimit = 0.0

    for ((scale, limit) in ranges) {
        val scaleValue = scale.toDouble()
        val limitValue = limit.toDouble()
        val segment = limitValue - lastLimit
        if (this <= limitValue) {
            return accumulated + scaleValue * (this - lastLimit)
        }
        accumulated += scaleValue * segment
        lastLimit = limitValue
    }

    // When we're here, it means the value is larger than all limits
    val (pastScale, pastLimit) = ranges.last()
    return accumulated + pastScale.toDouble() * (this - pastLimit.toDouble())
}

fun Float.scaledBy(vararg ranges: Pair<Number, Number>) = this.scaledBy(ranges.toList())

fun Float.scaledBy(ranges: List<Pair<Number, Number>>): Float {
    if (ranges.isEmpty()) return this

    var accumulated = 0f
    var lastLimit = 0f

    for ((scale, limit) in ranges) {
        val scaleValue = scale.toFloat()
        val limitValue = limit.toFloat()
        val segment = limitValue - lastLimit
        if (this <= limitValue) {
            return accumulated + scaleValue * (this - lastLimit)
        }
        accumulated += scaleValue * segment
        lastLimit = limitValue
    }

    // When we're here, it means the value is larger than all limits
    val (pastScale, pastLimit) = ranges.last()
    return accumulated + pastScale.toFloat() * (this - pastLimit.toFloat())
}
