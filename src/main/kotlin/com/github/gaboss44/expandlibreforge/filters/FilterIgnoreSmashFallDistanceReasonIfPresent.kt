package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.getFormattedStrings
import com.willfp.libreforge.triggers.TriggerData
import io.papermc.paper.event.entity.EntitySmashAttackFallDistanceEvent

object FilterIgnoreSmashFallDistanceReasonIfPresent : Filter<NoCompileData, Collection<String>>("ignore_smash_fall_distance_reason_if_present") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Collection<String> {
        return config.getFormattedStrings(key, data)
    }

    override fun isMet(data: TriggerData, value: Collection<String>, compileData: NoCompileData): Boolean {
        val event = data.event as? EntitySmashAttackFallDistanceEvent ?: return true
        return !value.containsIgnoreCase(event.reason.name)
    }
}