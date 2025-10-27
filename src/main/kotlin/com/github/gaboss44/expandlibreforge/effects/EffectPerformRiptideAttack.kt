package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.util.EntityTarget
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.HumanEntity

object EffectPerformRiptideAttack : Effect<NoCompileData>("perform_riptide_attack") {

    override val isPermanent = false

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val entity = EntityTarget[config.getString("entity")]?.getEntity(data) ?: data.player ?: return false
        if (entity !is HumanEntity) return false

        entity.performRiptideAttack(
            config.getDoubleFromExpression("strength", data).toFloat(),
            config.getIntFromExpression("ticks", data),
            config.getDoubleFromExpression("damage", data).toFloat(),
            data.item
        )

        return true
    }
}