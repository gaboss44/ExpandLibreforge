package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.player.PlayerItemDamageEvent

sealed class FilterOriginalDamageTakenByItem(id: String) : Filter<NoCompileData, Int>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Int {
        return config.getIntFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Int, compileData: NoCompileData): Boolean {
        val event = data.event as? PlayerItemDamageEvent ?: return true
        return compare(event.originalDamage, value)
    }

    abstract fun compare(itemDamage: Int, value: Int): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterOriginalDamageTakenByItem("original_damage_taken_by_item_equals") {
        override fun compare(itemDamage: Int, value: Int): Boolean {
            return itemDamage == value
        }
    }

    object AtLeast : FilterOriginalDamageTakenByItem("original_damage_taken_by_item_at_least") {
        override fun compare(itemDamage: Int, value: Int): Boolean {
            return itemDamage >= value
        }
    }

    object AtMost : FilterOriginalDamageTakenByItem("original_damage_taken_by_item_at_most") {
        override fun compare(itemDamage: Int, value: Int): Boolean {
            return itemDamage <= value
        }
    }

    object GreaterThan : FilterOriginalDamageTakenByItem("original_damage_taken_by_item_greater_than") {
        override fun compare(itemDamage: Int, value: Int): Boolean {
            return itemDamage > value
        }
    }

    object LowerThan : FilterOriginalDamageTakenByItem("original_damage_taken_by_item_lower_than") {
        override fun compare(itemDamage: Int, value: Int): Boolean {
            return itemDamage < value
        }
    }
}