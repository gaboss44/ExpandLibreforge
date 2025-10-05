package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import io.papermc.paper.event.player.PlayerAttackEntityCriticalCheckEvent

sealed class FilterCriticalMultiplier(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val event = data.event as? PlayerAttackEntityCriticalCheckEvent ?: return true
        return compare(event.criticalMultiplier.toDouble(), value)
    }

    abstract fun compare(criticalMultiplier: Double, value: Double): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterCriticalMultiplier("critical_multiplier_equals") {
        override fun compare(criticalMultiplier: Double, value: Double): Boolean {
            return criticalMultiplier == value
        }
    }

    object AtLeast : FilterCriticalMultiplier("critical_multiplier_at_least") {
        override fun compare(criticalMultiplier: Double, value: Double): Boolean {
            return criticalMultiplier >= value
        }
    }

    object AtMost : FilterCriticalMultiplier("critical_multiplier_at_most") {
        override fun compare(criticalMultiplier: Double, value: Double): Boolean {
            return criticalMultiplier <= value
        }
    }

    object GreaterThan : FilterCriticalMultiplier("critical_multiplier_greater_than") {
        override fun compare(criticalMultiplier: Double, value: Double): Boolean {
            return criticalMultiplier > value
        }
    }

    object LowerThan : FilterCriticalMultiplier("critical_multiplier_lower_than") {
        override fun compare(criticalMultiplier: Double, value: Double): Boolean {
            return criticalMultiplier < value
        }
    }
}
