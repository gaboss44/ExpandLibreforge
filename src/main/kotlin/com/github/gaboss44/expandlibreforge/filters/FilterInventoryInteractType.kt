package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.inventory.TradeSelectEvent

object FilterInventoryInteractType : Filter<Collection<String>, Collection<String>>("inventory_interact_type") {

    override fun makeCompileData(
        config: Config,
        context: ViolationContext,
        values: Collection<String>
    ): Collection<String> {
        return values
    }

    override fun getValue(
        config: Config,
        data: TriggerData?,
        key: String
    ): Collection<String> {
        return config.getStrings(key)
    }

    override fun isMet(
        data: TriggerData,
        value: Collection<String>,
        compileData: Collection<String>
    ): Boolean {
        val event = data.event as? InventoryInteractEvent ?: return false
        val type = Values.getFromEvent(event) ?: return false
        return compileData.contains(type.lowerValue)
    }

    enum class Values(val lowerValue : String) {
        CLICK("click"),
        DRAG("drag"),
        TRADE_SELECT("trade_select");

        companion object {
            private val byLowerValue = entries.associateBy { it.lowerValue }

            operator fun get(value: String) = byLowerValue[value.lowercase()]

            fun getFromEvent(event: InventoryInteractEvent) = when (event) {
                is InventoryClickEvent -> CLICK
                is InventoryDragEvent -> DRAG
                is TradeSelectEvent -> TRADE_SELECT
                else -> null
            }
        }
    }
}