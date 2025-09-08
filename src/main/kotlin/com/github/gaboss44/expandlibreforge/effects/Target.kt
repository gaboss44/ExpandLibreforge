package com.github.gaboss44.expandlibreforge.effects

import com.willfp.libreforge.triggers.TriggerData
import kotlin.collections.get

enum class Target(val lowerValue: String) {
    PLAYER("player"),
    VICTIM("victim"),
    PROJECTILE("projectile");

    fun getEntity(data: TriggerData) = when(this) {
        PLAYER -> data.player
        VICTIM -> data.victim
        PROJECTILE -> data.projectile
    }

    companion object {
        private val byLowerValue = entries.associateBy { it.lowerValue }

        operator fun get(value: String?) = byLowerValue[value?.lowercase()]
    }
}