package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.util.EntityTarget
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.filterNotEmpty
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.getStrings
import com.willfp.libreforge.slot.SlotType
import com.willfp.libreforge.slot.SlotTypes
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.LivingEntity

object FilterItemsInSlot : Filter<Pair<Collection<TestableItem>, List<SlotType>>, Config>("items_in_slot") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Config {
        return config.getSubsection(key)
    }

    override fun isMet(data: TriggerData, value: Config, compileData: Pair<Collection<TestableItem>, List<SlotType>>): Boolean {
        val entity = EntityTarget[value.getString("entity")]?.getEntity(data) as? LivingEntity ?: data.player ?: return true

        return compileData.second.any { slot -> slot.getItems(entity).filterNotEmpty().any { compileData.first.any { test -> test.matches(it) } } }
    }

    override fun makeCompileData(
        config: Config, context: ViolationContext, values: Config
    ): Pair<Collection<TestableItem>, List<SlotType>> {
        return values.getStrings("items", "item").map { Items.lookup(it) }.filterNot { item -> item is EmptyTestableItem } to values.getStrings("slots", "slot").mapNotNull { SlotTypes[it] }
    }
}
