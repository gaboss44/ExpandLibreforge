package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData

abstract class PlayerNoDamageTicksFilter(id: String) : Filter<NoCompileData, Int>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Int {
        return config.getIntFromExpression(key)
    }

    final override fun isMet(data: TriggerData, value: Int, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false
        return compare(player.noDamageTicks, value)
    }

    abstract fun compare(playerValue: Int, filterValue: Int): Boolean
}