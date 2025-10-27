package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import io.papermc.paper.event.entity.EntityDamageArmorAbsorbEvent

sealed class FilterAbsorbArmorPoints(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDamageArmorAbsorbEvent ?: return true
        return compare(event.armorPoints.toDouble(), value)
    }

    abstract fun compare(armorValue: Double, value: Double): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterAbsorbArmorPoints("absorb_armor_points_equals") {
        override fun compare(armorValue: Double, value: Double): Boolean {
            return armorValue == value
        }
    }

    object AtLeast : FilterAbsorbArmorPoints("absorb_armor_points_at_least") {
        override fun compare(armorValue: Double, value: Double): Boolean {
            return armorValue >= value
        }
    }

    object AtMost : FilterAbsorbArmorPoints("absorb_armor_points_at_most") {
        override fun compare(armorValue: Double, value: Double): Boolean {
            return armorValue <= value
        }
    }

    object GreaterThan : FilterAbsorbArmorPoints("absorb_armor_points_greater_than") {
        override fun compare(armorValue: Double, value: Double): Boolean {
            return armorValue > value
        }
    }

    object LowerThan : FilterAbsorbArmorPoints("absorb_armor_points_lower_than") {
        override fun compare(armorValue: Double, value: Double): Boolean {
            return armorValue < value
        }
    }
}
