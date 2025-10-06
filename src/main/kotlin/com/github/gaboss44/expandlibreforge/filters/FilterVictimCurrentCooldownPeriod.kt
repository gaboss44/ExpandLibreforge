package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.Player

sealed class FilterVictimCurrentCooldownPeriod(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val victim = data.victim as? Player ?: return true
        return compare(victim.cooldownPeriod.toDouble(), value)
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

    object Equals : FilterVictimCurrentCooldownPeriod("victim_current_cooldown_period_equals") {
        override fun compare(cooldownPeriod: Double, value: Double): Boolean {
            return cooldownPeriod == value
        }
    }

    object AtLeast : FilterVictimCurrentCooldownPeriod("victim_current_cooldown_period_at_least") {
        override fun compare(cooldownPeriod: Double, value: Double): Boolean {
            return cooldownPeriod >= value
        }
    }

    object AtMost : FilterVictimCurrentCooldownPeriod("victim_current_cooldown_period_at_most") {
        override fun compare(cooldownPeriod: Double, value: Double): Boolean {
            return cooldownPeriod <= value
        }
    }

    object GreaterThan : FilterVictimCurrentCooldownPeriod("victim_current_cooldown_period_greater_than") {
        override fun compare(cooldownPeriod: Double, value: Double): Boolean {
            return cooldownPeriod > value
        }
    }

    object LowerThan : FilterVictimCurrentCooldownPeriod("victim_current_cooldown_period_lower_than") {
        override fun compare(cooldownPeriod: Double, value: Double): Boolean {
            return cooldownPeriod < value
        }
    }
}