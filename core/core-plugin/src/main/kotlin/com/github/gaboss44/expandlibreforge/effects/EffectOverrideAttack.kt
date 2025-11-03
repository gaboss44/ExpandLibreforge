package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.extensions.performAttackSafely
import com.github.gaboss44.expandlibreforge.util.getEquipmentSlot
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent

object EffectOverrideAttack : Effect<NoCompileData>("override_attack") {

    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? PrePlayerAttackEntityEvent ?: return false
        if (!event.willAttack()) return false

        val damage = if (!config.has("damage")) null else {
            config.getDoubleFromExpression("damage", data)
        }

        val slot = getEquipmentSlot(config.getString("slot"))

        event.isCancelled = true

        event.player.performAttackSafely(event.attacked, damage?.toFloat(), slot)

        return true
    }
}