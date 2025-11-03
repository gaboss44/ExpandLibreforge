package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.player.PlayerItemDamageEvent

object EffectSetDamageTakenByItem : Effect<NoCompileData>("set_damage_taken_by_item") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("value", "You must set a value to set the damage")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? PlayerItemDamageEvent ?: return false
        val valueStr = config.getStringOrNull("value") ?: return false
        val item = event.item

        val damageStr = event.damage.toString()
        val originalDamageStr = event.originalDamage.toString()
        val itemDamageStr = item.durability.toString()
        val itemMaxDamageStr = item.type.maxDurability.toString()

        val expr = valueStr
            .replace("%damage%",damageStr)
            .replace("%d%",damageStr)
            .replace("%original_damage%",originalDamageStr)
            .replace("%ogd%",originalDamageStr)
            .replace("%item_damage%",itemDamageStr)
            .replace("%itd%",itemDamageStr)
            .replace("%item_max_damage%",itemMaxDamageStr)
            .replace("%imd%",itemMaxDamageStr)

        event.damage = NumberUtils.evaluateExpression(expr, config.toPlaceholderContext(data)).toInt()
        return true
    }
}