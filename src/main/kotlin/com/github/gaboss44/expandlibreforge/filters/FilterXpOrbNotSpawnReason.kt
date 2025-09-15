package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.util.XpOrbFilter
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.ExperienceOrb

object FilterXpOrbNotSpawnReason : XpOrbFilter<NoCompileData, Collection<String>>("xp_orb_not_spawn_reason") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Collection<String> {
        return config.getStrings(key)
    }

    override fun isMet(
        data: TriggerData,
        value: Collection<String>,
        compileData: NoCompileData,
        orb: ExperienceOrb
    ): Boolean {
        return !value.containsIgnoreCase(orb.spawnReason.name)
    }
}