package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData

object FilterPlayerIsSilent : Filter<NoCompileData,Boolean>("player_is_silent") {
    override fun getValue(
        config: Config,
        data: TriggerData?,
        key: String
    ) = config.getBoolOrNull(key) ?: true

    override fun isMet(data: TriggerData, value: Boolean, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false
        return player.isSilent == value
    }
}