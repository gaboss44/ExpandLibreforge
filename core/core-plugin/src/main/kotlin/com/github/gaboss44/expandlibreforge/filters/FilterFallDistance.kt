package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData

sealed class FilterFallDistance(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val player = data.player ?: return true
        return compare(player.fallDistance.toDouble(), value)
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

    object Equals : FilterFallDistance("fall_distance_equals") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance == value
        }
    }

    object AtLeast : FilterFallDistance("fall_distance_at_least") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance >= value
        }
    }

    object AtMost : FilterFallDistance("fall_distance_at_most") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance <= value
        }
    }

    object GreaterThan : FilterFallDistance("fall_distance_greater_than") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance > value
        }
    }

    object LowerThan : FilterFallDistance("fall_distance_lower_than") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance < value
        }
    }
}