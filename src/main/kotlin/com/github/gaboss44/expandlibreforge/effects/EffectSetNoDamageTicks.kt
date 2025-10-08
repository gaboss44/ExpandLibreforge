package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.util.EntityTarget
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.LivingEntity

object EffectSetNoDamageTicks : Effect<NoCompileData>("set_no_damage_ticks") {

    override val isPermanent = false

    override val arguments = arguments {
        require("value", "You must specify the value.")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val target = EntityTarget[config.getString("target")] ?: EntityTarget.PLAYER
        val entity = target.getEntity(data) as? LivingEntity ?: return false
        val noDamageTicksStr = entity.noDamageTicks.toString()
        val expr = config.getString("value")
            .replace("%no_damage_ticks%", noDamageTicksStr)
            .replace("%ndt%", noDamageTicksStr)
        val value = NumberUtils.evaluateExpression(expr, config.toPlaceholderContext(data))
        val percentually = config.getBool("percentually")
        if (percentually) {
            entity.noDamageTicks = (value * entity.maximumNoDamageTicks / 100).toInt()
        }
        else {
            entity.noDamageTicks = value.toInt()
        }
        return true
    }
}