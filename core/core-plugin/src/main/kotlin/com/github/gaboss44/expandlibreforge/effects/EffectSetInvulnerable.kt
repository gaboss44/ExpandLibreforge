package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData

object EffectSetInvulnerable : Effect<NoCompileData>("set_invulnerable") {

    override val isPermanent = false

    override val arguments = arguments {
        require("value", "You must specify the value.")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val target = Target[config.getString("target")] ?: Target.PLAYER
        val entity = target.getEntity(data) ?: return false
        val value = config.getBoolOrNull("value") ?: true
        entity.isInvulnerable = value
        return true
    }

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
}