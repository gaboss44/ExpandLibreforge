package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData

sealed class FilterPlayerCurrentCooldownPeriod(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val player = data.player ?: return true
        return compare(player.cooldownPeriod.toDouble(), value)
    }

    abstract fun compare(cooldownPeriod: Double, value: Double): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterPlayerCurrentCooldownPeriod("player_current_cooldown_period_equals") {
        override fun compare(cooldownPeriod: Double, value: Double): Boolean {
            return cooldownPeriod == value
        }
    }

    object AtLeast : FilterPlayerCurrentCooldownPeriod("player_current_cooldown_period_at_least") {
        override fun compare(cooldownPeriod: Double, value: Double): Boolean {
            return cooldownPeriod >= value
        }
    }

    object AtMost : FilterPlayerCurrentCooldownPeriod("player_current_cooldown_period_at_most") {
        override fun compare(cooldownPeriod: Double, value: Double): Boolean {
            return cooldownPeriod <= value
        }
    }

    object GreaterThan : FilterPlayerCurrentCooldownPeriod("player_current_cooldown_period_greater_than") {
        override fun compare(cooldownPeriod: Double, value: Double): Boolean {
            return cooldownPeriod > value
        }
    }

    object LowerThan : FilterPlayerCurrentCooldownPeriod("player_current_cooldown_period_lower_than") {
        override fun compare(cooldownPeriod: Double, value: Double): Boolean {
            return cooldownPeriod < value
        }
    }
}