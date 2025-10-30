package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData

sealed class FilterPlayerNoDamageTicks(id: String) : Filter<NoCompileData, Int>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Int {
        return config.getIntFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Int, compileData: NoCompileData): Boolean {
        val player = data.player ?: return true
        return compare(player.noDamageTicks, value)
    }

    abstract fun compare(playerValue: Int, filterValue: Int): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterPlayerNoDamageTicks("player_no_damage_ticks_equals") {
        override fun compare(playerValue: Int, filterValue: Int): Boolean {
            return playerValue == filterValue
        }
    }

    object AtLeast : FilterPlayerNoDamageTicks("player_no_damage_ticks_at_least") {
        override fun compare(playerValue: Int, filterValue: Int): Boolean {
            return playerValue >= filterValue
        }
    }

    object AtMost : FilterPlayerNoDamageTicks("player_no_damage_ticks_at_most") {
        override fun compare(playerValue: Int, filterValue: Int): Boolean {
            return playerValue <= filterValue
        }
    }

    object GreaterThan : FilterPlayerNoDamageTicks("player_no_damage_ticks_greater_than") {
        override fun compare(playerValue: Int, filterValue: Int): Boolean {
            return playerValue > filterValue
        }
    }

    object LowerThan : FilterPlayerNoDamageTicks("player_no_damage_ticks_lower_than") {
        override fun compare(playerValue: Int, filterValue: Int): Boolean {
            return playerValue < filterValue
        }
    }
}