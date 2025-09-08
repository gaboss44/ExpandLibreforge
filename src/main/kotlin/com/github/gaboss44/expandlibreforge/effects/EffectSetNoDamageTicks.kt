package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.LivingEntity

object EffectSetNoDamageTicks : Effect<NoCompileData>("set_no_damage_ticks") {

    override val isPermanent = false

    override val arguments = arguments {
        require("value", "You must specify the value.")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val target = Target[config.getString("target")] ?: Target.PLAYER
        val entity = target.getEntity(data) as? LivingEntity ?: return false
        val value = config.getIntFromExpression("value")
        entity.noDamageTicks = value
        return true
    }
}