package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.inventory.InventoryClickEvent

object FilterInventoryAction : Filter<NoCompileData, Collection<String>>("inventory_action") {
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
        compileData: NoCompileData
    ): Boolean {
        val action = (data.event as? InventoryClickEvent)?.action ?: return false
        return value.containsIgnoreCase(action.name)
    }
}