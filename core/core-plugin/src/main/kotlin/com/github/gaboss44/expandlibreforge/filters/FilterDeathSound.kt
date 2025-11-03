package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.event.entity.EntityDeathEvent

object FilterDeathSound : Filter<NoCompileData, Collection<String>>("death_sound") {
    override fun getValue(
        config: Config,
        data: TriggerData?,
        key: String
    ): Collection<String> {
        return config.getStrings(key)
    }

    override fun isMet(
        data: TriggerData,
        value: Collection<String>,
        compileData: NoCompileData
    ): Boolean {
        val event = data.event as? EntityDeathEvent ?: return true
        val sound = event.deathSound ?: return false
        return value.any { Registry.SOUNDS.getOrThrow(NamespacedKey.minecraft(it)) == sound }
    }
}
