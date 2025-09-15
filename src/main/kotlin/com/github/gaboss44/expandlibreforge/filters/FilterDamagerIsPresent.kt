package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.entity.EntityDamageByEntityEvent

object FilterDamagerIsPresent : Filter<NoCompileData, Boolean>("damager_is_present") {
    override fun getValue(
        config: Config,
        data: TriggerData?,
        key: String
    ): Boolean {
        return config.getBool(key)
    }

    override fun isMet(
        data: TriggerData,
        value: Boolean,
        compileData: NoCompileData
    ): Boolean {
        return (data.event as? EntityDamageByEntityEvent != null) == value
    }

}