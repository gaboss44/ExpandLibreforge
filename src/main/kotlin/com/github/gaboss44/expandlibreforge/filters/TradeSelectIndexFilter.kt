package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.inventory.TradeSelectEvent

abstract class TradeSelectIndexFilter(id: String) : Filter<NoCompileData, Int>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Int {
        return config.getIntFromExpression(key)
    }

    final override fun isMet(data: TriggerData, value: Int, compileData: NoCompileData): Boolean {
        val event = data.event as? TradeSelectEvent ?: return false
        return compare(event.index, value)
    }

    abstract fun compare(eventIndex: Int, filterIndex: Int): Boolean
}

