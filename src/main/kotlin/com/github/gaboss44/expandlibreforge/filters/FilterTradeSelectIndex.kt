package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.inventory.TradeSelectEvent

sealed class FilterTradeSelectIndex(id: String) : Filter<NoCompileData, Int>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Int {
        return config.getIntFromExpression(key)
    }

    final override fun isMet(data: TriggerData, value: Int, compileData: NoCompileData): Boolean {
        val event = data.event as? TradeSelectEvent ?: return true
        return compare(event.index, value)
    }

    abstract fun compare(eventIndex: Int, filterIndex: Int): Boolean

    object Equals : FilterTradeSelectIndex("trade_select_index_equals") {
        override fun compare(eventIndex: Int, filterIndex: Int): Boolean {
            return eventIndex == filterIndex
        }
    }

    object AtLeast : FilterTradeSelectIndex("trade_select_index_at_least") {
        override fun compare(eventIndex: Int, filterIndex: Int): Boolean {
            return eventIndex >= filterIndex
        }
    }

    object AtMost : FilterTradeSelectIndex("trade_select_index_at_most") {
        override fun compare(eventIndex: Int, filterIndex: Int): Boolean {
            return eventIndex <= filterIndex
        }
    }

    object GreaterThan : FilterTradeSelectIndex("trade_select_index_greater_than") {
        override fun compare(eventIndex: Int, filterIndex: Int): Boolean {
            return eventIndex > filterIndex
        }
    }

    object LowerThan : FilterTradeSelectIndex("trade_select_index_lower_than") {
        override fun compare(eventIndex: Int, filterIndex: Int): Boolean {
            return eventIndex < filterIndex
        }
    }
}
