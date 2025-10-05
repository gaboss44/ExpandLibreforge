package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import io.papermc.paper.event.player.PlayerAttackEntityCriticalCheckEvent

object FilterWasOriginallyCritical : Filter<NoCompileData, Boolean>("was_originally_critical") {
    override fun getValue(
        config: Config,
        data: TriggerData?,
        key: String
    ) = config.getBoolOrNull(key) ?: true

    override fun isMet(data: TriggerData, value: Boolean, compileData: NoCompileData): Boolean {
        val event = data.event as? PlayerAttackEntityCriticalCheckEvent ?: return false
        return event.wasOriginallyCritical() == value
    }
}