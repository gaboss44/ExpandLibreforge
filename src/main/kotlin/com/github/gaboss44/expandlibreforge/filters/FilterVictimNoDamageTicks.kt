package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData

sealed class FilterVictimNoDamageTicks(id: String) : Filter<NoCompileData, Int>(id) {
    final override fun getValue(config: Config, data: TriggerData?, key: String): Int {
        return config.getIntFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Int, compileData: NoCompileData): Boolean {
        val victim = data.victim ?: return true
        return compare(victim.noDamageTicks, value)
    }

    abstract fun compare(victimValue: Int, filterValue: Int): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object AtMost : FilterVictimNoDamageTicks("victim_no_damage_ticks_at_most") {
        override fun compare(victimValue: Int, filterValue: Int): Boolean {
            return victimValue <= filterValue
        }
    }

    object AtLeast : FilterVictimNoDamageTicks("victim_no_damage_ticks_at_least") {
        override fun compare(victimValue: Int, filterValue: Int): Boolean {
            return victimValue >= filterValue
        }
    }

    object Equals : FilterVictimNoDamageTicks("victim_no_damage_ticks_equals") {
        override fun compare(victimValue: Int, filterValue: Int): Boolean {
            return victimValue == filterValue
        }
    }

    object GreaterThan : FilterVictimNoDamageTicks("victim_no_damage_ticks_greater_than") {
        override fun compare(victimValue: Int, filterValue: Int): Boolean {
            return victimValue > filterValue
        }
    }

    object LowerThan : FilterVictimNoDamageTicks("victim_no_damage_ticks_lower_than") {
        override fun compare(victimValue: Int, filterValue: Int): Boolean {
            return victimValue < filterValue
        }
    }
}