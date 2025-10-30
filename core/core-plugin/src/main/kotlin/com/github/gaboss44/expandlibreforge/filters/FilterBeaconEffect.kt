package com.github.gaboss44.expandlibreforge.filters

import com.destroystokyo.paper.event.block.BeaconEffectEvent
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData

object FilterBeaconEffect : Filter<NoCompileData, Collection<String>>("beacon_effect") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Collection<String> {
        return config.getStrings(key)
    }

    override fun isMet(data: TriggerData, value: Collection<String>, compileData: NoCompileData): Boolean {
        val event = data.event as? BeaconEffectEvent ?: return true

        return value.containsIgnoreCase(event.effect.type.key.key)
    }
}