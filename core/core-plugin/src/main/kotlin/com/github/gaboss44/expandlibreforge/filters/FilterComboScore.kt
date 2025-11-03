package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.events.combo.ComboEvent
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData

sealed class FilterComboScore(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val event = data.event as? ComboEvent ?: return true
        return compare(event.combo.score, value)
    }

    abstract fun compare(comboValue: Double, filterValue: Double): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterComboScore("combo_score_equals") {
        override fun compare(comboValue: Double, filterValue: Double): Boolean {
            return comboValue == filterValue
        }
    }

    object AtLeast : FilterComboScore("combo_score_at_least") {
        override fun compare(comboValue: Double, filterValue: Double): Boolean {
            return comboValue >= filterValue
        }
    }

    object AtMost : FilterComboScore("combo_score_at_most") {
        override fun compare(comboValue: Double, filterValue: Double): Boolean {
            return comboValue <= filterValue
        }
    }

    object GreaterThan : FilterComboScore("combo_score_greater_than") {
        override fun compare(comboValue: Double, filterValue: Double): Boolean {
            return comboValue > filterValue
        }
    }

    object LowerThan : FilterComboScore("combo_score_lower_than") {
        override fun compare(comboValue: Double, filterValue: Double): Boolean {
            return comboValue < filterValue
        }
    }
}