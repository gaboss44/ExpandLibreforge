package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import io.papermc.paper.event.entity.EntitySmashAttackFallDistanceEvent

sealed class FilterSmashFallDistance(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val event = data.event as? EntitySmashAttackFallDistanceEvent ?: return true
        return compare(event.fallDistance, value)
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

    object Equals : FilterSmashFallDistance("smash_fall_distance_equals") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance == value
        }
    }

    object AtLeast : FilterSmashFallDistance("smash_fall_distance_at_least") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance >= value
        }
    }

    object AtMost : FilterSmashFallDistance("smash_fall_distance_at_most") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance <= value
        }
    }

    object GreaterThan : FilterSmashFallDistance("smash_fall_distance_greater_than") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance > value
        }
    }

    object LowerThan : FilterSmashFallDistance("smash_fall_distance_lower_than") {
        override fun compare(fallDistance: Double, value: Double): Boolean {
            return fallDistance < value
        }
    }
}