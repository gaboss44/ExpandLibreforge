package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData

sealed class FilterAttackStrengthTicker(id: String) : Filter<NoCompileData, Int>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Int {
        return config.getIntFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Int, compileData: NoCompileData): Boolean {
        val player = data.player ?: return true
        return compare(player.attackStrengthTicker, value)
    }

    abstract fun compare(strengthTicks: Int, value: Int): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterAttackStrengthTicker("attack_strength_ticker_equals") {
        override fun compare(strengthTicks: Int, value: Int): Boolean {
            return strengthTicks == value
        }
    }

    object AtLeast : FilterAttackStrengthTicker("attack_strength_ticker_at_least") {
        override fun compare(strengthTicks: Int, value: Int): Boolean {
            return strengthTicks >= value
        }
    }

    object AtMost : FilterAttackStrengthTicker("attack_strength_ticker_at_most") {
        override fun compare(strengthTicks: Int, value: Int): Boolean {
            return strengthTicks <= value
        }
    }

    object GreaterThan : FilterAttackStrengthTicker("attack_strength_ticker_greater_than") {
        override fun compare(strengthTicks: Int, value: Int): Boolean {
            return strengthTicks > value
        }
    }

    object LowerThan : FilterAttackStrengthTicker("attack_strength_ticker_lower_than") {
        override fun compare(strengthTicks: Int, value: Int): Boolean {
            return strengthTicks < value
        }
    }
}