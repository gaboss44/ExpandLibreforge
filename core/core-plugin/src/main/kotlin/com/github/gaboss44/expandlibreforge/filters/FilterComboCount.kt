package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.events.combo.ComboEvent
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData

sealed class FilterComboCount(id: String) : Filter<NoCompileData, Int>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Int {
        return config.getIntFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Int, compileData: NoCompileData): Boolean {
        val event = data.event as? ComboEvent ?: return true
        return compare(event.combo.count, value)
    }

    abstract fun compare(comboValue: Int, filterValue: Int): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterComboCount("combo_count_equals") {
        override fun compare(comboValue: Int, filterValue: Int): Boolean {
            return comboValue == filterValue
        }
    }

    object AtLeast : FilterComboCount("combo_count_at_least") {
        override fun compare(comboValue: Int, filterValue: Int): Boolean {
            return comboValue >= filterValue
        }
    }

    object AtMost : FilterComboCount("combo_count_at_most") {
        override fun compare(comboValue: Int, filterValue: Int): Boolean {
            return comboValue <= filterValue
        }
    }

    object GreaterThan : FilterComboCount("combo_count_greater_than") {
        override fun compare(comboValue: Int, filterValue: Int): Boolean {
            return comboValue > filterValue
        }
    }

    object LowerThan : FilterComboCount("combo_count_lower_than") {
        override fun compare(comboValue: Int, filterValue: Int): Boolean {
            return comboValue < filterValue
        }
    }
}