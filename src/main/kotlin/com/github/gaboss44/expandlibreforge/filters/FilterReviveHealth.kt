package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.entity.EntityDeathEvent

sealed class FilterReviveHealth(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDeathEvent ?: return true
        return compare(event.reviveHealth, value)
    }

    abstract fun compare(reviveHealth: Double, value: Double): Boolean

    object Equals : FilterReviveHealth("revive_health_equals") {
        override fun compare(reviveHealth: Double, value: Double): Boolean {
            return reviveHealth == value
        }
    }

    object AtLeast : FilterReviveHealth("revive_health_at_least") {
        override fun compare(reviveHealth: Double, value: Double): Boolean {
            return reviveHealth >= value
        }
    }

    object AtMost : FilterReviveHealth("revive_health_at_most") {
        override fun compare(reviveHealth: Double, value: Double): Boolean {
            return reviveHealth <= value
        }
    }

    object GreaterThan : FilterReviveHealth("revive_health_greater_than") {
        override fun compare(reviveHealth: Double, value: Double): Boolean {
            return reviveHealth > value
        }
    }

    object LowerThan : FilterReviveHealth("revive_health_lower_than") {
        override fun compare(reviveHealth: Double, value: Double): Boolean {
            return reviveHealth < value
        }
    }
}