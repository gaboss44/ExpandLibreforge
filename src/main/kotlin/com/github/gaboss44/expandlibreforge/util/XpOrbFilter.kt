package com.github.gaboss44.expandlibreforge.util

import com.willfp.eco.core.events.NaturalExpGainEvent
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.ExperienceOrb
import org.bukkit.event.player.PlayerExpChangeEvent

abstract class XpOrbFilter<T,V>(id: String) : Filter<T, V>(id) {

    final override fun isMet(data: TriggerData, value: V, compileData: T): Boolean {
        val event = data.event ?: return true
        val orb = when (event) {
            is PlayerExpChangeEvent -> event.source as? ExperienceOrb ?: return true
            is NaturalExpGainEvent -> event.expChangeEvent.source as? ExperienceOrb ?: return true
            else -> return true
        }
        return isMet(data, value, compileData, orb)
    }

    abstract fun isMet(data: TriggerData, value: V, compileData: T, orb: ExperienceOrb): Boolean
}