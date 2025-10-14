package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.util.EntityTarget
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.Player

object FilterEntityInteractivity : Filter<NoCompileData, Config>("entity_interactivity") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Config {
        return config.getSubsection(key)
    }

    override fun isMet(data: TriggerData, value: Config, compileData: NoCompileData): Boolean {
        val interactor = EntityTarget[value.getString("interactor")]?.getEntity(data) as? Player ?: return false
        val target = EntityTarget[value.getString("target")]?.getEntity(data) ?: return false

        return interactor.canInteractWithEntity(
            target,
            value.getDoubleFromExpression("distance", data)
        ) == value.getBool("value")
    }
}