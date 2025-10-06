package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.util.EntityTarget
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.entity.Player

object EffectResetCurrentCooldownPeriod : Effect<NoCompileData>("reset_current_cooldown_period") {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.PROJECTILE
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val target = EntityTarget[config.getString("target")] ?: EntityTarget.PLAYER
        val entity = target.getEntity(data) as? Player ?: return false
        entity.resetCooldown()
        return true
    }
}