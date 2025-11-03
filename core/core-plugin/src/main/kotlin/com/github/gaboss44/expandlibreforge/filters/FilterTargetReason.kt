package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.entity.EntityTargetEvent

object FilterTargetReason : Filter<NoCompileData, Collection<String>>("target_reason") {
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
        val event = data.event as? EntityTargetEvent ?: return false
        return value.containsIgnoreCase(event.reason.name)
    }

}