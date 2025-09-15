package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.player.PlayerExpChangeEvent

sealed class FilterXpChange(id: String) : Filter<NoCompileData, Double>(id) {
    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val event = data.event as? PlayerExpChangeEvent ?: return true
        return compare(event.amount.toDouble(), value)
    }

    abstract fun compare(eventValue: Double, filterValue: Double): Boolean

    object Equals : FilterXpChange("xp_change_equals") {
        override fun compare(eventValue: Double, filterValue: Double): Boolean {
            return eventValue == filterValue
        }
    }

    object AtLeast : FilterXpChange("xp_change_at_least") {
        override fun compare(eventValue: Double, filterValue: Double): Boolean {
            return eventValue >= filterValue
        }
    }

    object AtMost : FilterXpChange("xp_change_at_most") {
        override fun compare(eventValue: Double, filterValue: Double): Boolean {
            return eventValue <= filterValue
        }
    }

    object GreaterThan : FilterXpChange("xp_change_greater_than") {
        override fun compare(eventValue: Double, filterValue: Double): Boolean {
            return eventValue > filterValue
        }
    }

    object LowerThan : FilterXpChange("xp_change_lower_than") {
        override fun compare(eventValue: Double, filterValue: Double): Boolean {
            return eventValue < filterValue
        }
    }
}