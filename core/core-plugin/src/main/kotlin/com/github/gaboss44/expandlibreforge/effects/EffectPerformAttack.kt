package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.extensions.performAttackSafely
import com.github.gaboss44.expandlibreforge.util.getEquipmentSlot
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter

object EffectPerformAttack : Effect<NoCompileData>("perform_attack") {

    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false
        val victim = data.victim ?: return false

        val damage = if (!config.has("damage")) null else {
            config.getDoubleFromExpression("damage", data)
        }

        val slot = getEquipmentSlot(config.getString("slot"))

        player.performAttackSafely(victim, damage?.toFloat(), slot)

        return true
    }
}