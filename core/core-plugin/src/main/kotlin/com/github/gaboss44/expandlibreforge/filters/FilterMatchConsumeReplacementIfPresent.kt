package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.items.matches
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.player.PlayerItemConsumeEvent

object FilterMatchConsumeReplacementIfPresent : Filter<Collection<TestableItem>, Collection<String>>("match_consume_replacements_if_present") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Collection<String> {
        return config.getStrings(key)
    }

    override fun isMet(data: TriggerData, value: Collection<String>, compileData: Collection<TestableItem>): Boolean {
        val event = data.event as? PlayerItemConsumeEvent ?: return true
        val item = event.replacement ?: return true

        return compileData.matches(item)
    }

    override fun makeCompileData(
        config: Config, context: ViolationContext, values: Collection<String>
    ): Collection<TestableItem> {
        return values.map { Items.lookup(it) }
    }
}