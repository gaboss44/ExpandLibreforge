package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.entity.EntityExhaustionEvent

sealed class FilterExhaustion(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityExhaustionEvent ?: return true
        return compare(event.exhaustion.toDouble(), value)
    }

    abstract fun compare(exhaustion: Double, value: Double): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterExhaustion("exhaustion_equals") {
        override fun compare(exhaustion: Double, value: Double): Boolean {
            return exhaustion == value
        }
    }

    object AtLeast : FilterExhaustion("exhaustion_at_least") {
        override fun compare(exhaustion: Double, value: Double): Boolean {
            return exhaustion >= value
        }
    }

    object AtMost : FilterExhaustion("exhaustion_at_most") {
        override fun compare(exhaustion: Double, value: Double): Boolean {
            return exhaustion <= value
        }
    }

    object GreaterThan : FilterExhaustion("exhaustion_greater_than") {
        override fun compare(exhaustion: Double, value: Double): Boolean {
            return exhaustion > value
        }
    }

    object LowerThan : FilterExhaustion("exhaustion_lower_than") {
        override fun compare(exhaustion: Double, value: Double): Boolean {
            return exhaustion < value
        }
    }
}