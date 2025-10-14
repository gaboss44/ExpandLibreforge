package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData

sealed class FilterVictimFallDistance(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val victim = data.victim ?: return true
        return compare(victim.fallDistance.toDouble(), value)
    }

    abstract fun compare(fallDistance: Double, value: Double): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterVictimFallDistance("victim_fall_distance_equals") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance == value
        }
    }

    object AtLeast : FilterVictimFallDistance("victim_fall_distance_at_least") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance >= value
        }
    }

    object AtMost : FilterVictimFallDistance("victim_fall_distance_at_most") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance <= value
        }
    }

    object GreaterThan : FilterVictimFallDistance("victim_fall_distance_greater_than") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance > value
        }
    }

    object LowerThan : FilterVictimFallDistance("victim_fall_distance_lower_than") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance < value
        }
    }
}