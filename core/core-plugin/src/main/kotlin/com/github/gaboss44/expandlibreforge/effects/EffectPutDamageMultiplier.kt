package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.features.multipliers.DamageMultipliers
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NamedValue
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.entity.EntityDamageEvent

object EffectPutDamageMultiplier : Effect<NoCompileData>("put_damage_multiplier") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("name", "You must specify a damage multiplier name")
        require("multiplier", "You must specify a damage multiplier value")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDamageEvent ?: return false
        val name = config.getFormattedString("multiplier")
        val multiplier = DamageMultipliers.get(event, name) ?: 1.0
        config.addInjectablePlaceholder(
            NamedValue(
                listOf("multiplier", "m"),
                multiplier
            ).placeholders
        )
        val newMultiplier = config.getDoubleFromExpression("multiplier", data)
        DamageMultipliers.put(event, name, newMultiplier)
        return true
    }
}