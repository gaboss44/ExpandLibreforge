package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent

object FilterDamageType : Filter<NoCompileData, Collection<String>>("damage_type") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Collection<String> {
        return config.getStrings(key)
    }

    override fun isMet(data: TriggerData, value: Collection<String>, compileData: NoCompileData): Boolean {
        val event = data.event ?: return true
        val source = when (event) {
            is EntityDamageEvent -> event.damageSource
            is EntityDeathEvent -> event.damageSource
            else -> return true
        }
        return value.containsIgnoreCase(source.damageType.key.key)
    }
}