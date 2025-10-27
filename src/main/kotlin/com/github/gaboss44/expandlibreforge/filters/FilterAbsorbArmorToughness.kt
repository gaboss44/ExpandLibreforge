package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import io.papermc.paper.event.entity.EntityDamageArmorAbsorbEvent

sealed class FilterAbsorbArmorToughness(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDamageArmorAbsorbEvent ?: return true
        return compare(event.armorToughness.toDouble(), value)
    }

    abstract fun compare(armorToughnessValue: Double, value: Double): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterAbsorbArmorToughness("absorb_armor_toughness_equals") {
        override fun compare(armorToughnessValue: Double, value: Double): Boolean {
            return armorToughnessValue == value
        }
    }

    object AtLeast : FilterAbsorbArmorToughness("absorb_armor_toughness_at_least") {
        override fun compare(armorToughnessValue: Double, value: Double): Boolean {
            return armorToughnessValue >= value
        }
    }

    object AtMost : FilterAbsorbArmorToughness("absorb_armor_toughness_at_most") {
        override fun compare(armorToughnessValue: Double, value: Double): Boolean {
            return armorToughnessValue <= value
        }
    }

    object GreaterThan : FilterAbsorbArmorToughness("absorb_armor_toughness_greater_than") {
        override fun compare(armorToughnessValue: Double, value: Double): Boolean {
            return armorToughnessValue > value
        }
    }

    object LowerThan : FilterAbsorbArmorToughness("absorb_armor_toughness_lower_than") {
        override fun compare(armorToughnessValue: Double, value: Double): Boolean {
            return armorToughnessValue < value
        }
    }
}

