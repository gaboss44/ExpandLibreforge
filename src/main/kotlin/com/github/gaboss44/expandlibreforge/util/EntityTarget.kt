package com.github.gaboss44.expandlibreforge.util

import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.Entity
import kotlin.collections.get

enum class EntityTarget(val lowerValue: String, private val provider: (TriggerData) -> Entity?) {
    PLAYER("player", { data -> data.player }),
    VICTIM("victim", { data -> data.victim }),
    PROJECTILE("projectile", { data -> data.projectile });

    fun getEntity(data: TriggerData) = provider.invoke(data)

    companion object {
        private val byLowerValue = entries.associateBy { it.lowerValue }

        operator fun get(value: String?) = byLowerValue[value?.lowercase()]
    }
}