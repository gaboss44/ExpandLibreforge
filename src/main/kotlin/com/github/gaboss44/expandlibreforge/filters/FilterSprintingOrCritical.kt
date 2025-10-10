package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

object FilterSprintingOrCritical : Filter<NoCompileData, Boolean>("sprinting_or_critical") {
    override fun getValue(
        config: Config,
        data: TriggerData?,
        key: String
    ) = config.getBoolOrNull(key) ?: true

    override fun isMet(data: TriggerData, value: Boolean, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDamageByEntityEvent ?: return true
        return (event.isCritical || (event.damager as? Player)?.isSprinting ?: false) == value
    }
}