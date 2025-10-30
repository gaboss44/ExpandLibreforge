package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.inventory.InventoryDragEvent

object FilterInventoryDragType : Filter<NoCompileData, String>("inventory_drag_type") {
    override fun getValue(
        config: Config,
        data: TriggerData?,
        key: String
    ) = config.getString(key)

    override fun isMet(
        data: TriggerData,
        value: String,
        compileData: NoCompileData
    ): Boolean {
        val event = data.event as? InventoryDragEvent ?: return false
        return event.type.name.equals(value, ignoreCase = true)
    }
}