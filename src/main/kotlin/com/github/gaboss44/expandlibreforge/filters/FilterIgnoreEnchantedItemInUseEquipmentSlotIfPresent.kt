package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import io.papermc.paper.event.entity.EntityEnchantedItemInUseEvent

object FilterIgnoreEnchantedItemInUseEquipmentSlotIfPresent : Filter<NoCompileData, Collection<String>>("ignore_enchanted_item_in_use_equipment_slot_if_present") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Collection<String> {
        return config.getStrings(key)
    }

    override fun isMet(data: TriggerData, value: Collection<String>, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityEnchantedItemInUseEvent ?: return true
        val slot = event.equipmentSlot ?: return true
        return value.containsIgnoreCase(slot.name)
    }
}