package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData

object FilterPlayerHasGravity : Filter<NoCompileData,Boolean>("player_has_gravity") {
    override fun getValue(
        config: Config,
        data: TriggerData?,
        key: String
    ) = config.getBool(key)

    override fun isMet(data: TriggerData, value: Boolean, compileData: NoCompileData): Boolean {
        val player = data.player ?: return true
        return player.hasGravity() == value
    }
}