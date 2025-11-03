package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.util.EntityTarget
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.LivingEntity

object FilterEntityOnGround : Filter<NoCompileData, Config>("entity_on_ground") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Config {
        return config.getSubsection(key)
    }

    override fun isMet(data: TriggerData, value: Config, compileData: NoCompileData): Boolean {
        val entity = EntityTarget[value.getString("entity")]?.getEntity(data) as? LivingEntity ?: data.player ?: return false
        return (entity).isOnGround == value.getBool("value")
    }
}