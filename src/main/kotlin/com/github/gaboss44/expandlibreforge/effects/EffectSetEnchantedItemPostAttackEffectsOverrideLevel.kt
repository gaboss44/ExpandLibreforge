package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import io.papermc.paper.event.entity.EntityEnchantedItemPostAttackEffectsEvent

object EffectSetEnchantedItemPostAttackEffectsOverrideLevel : Effect<NoCompileData>("set_enchanted_item_post_attack_effects_override_level") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("enchantment_override_level", "You must specify an enchantment override level")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityEnchantedItemPostAttackEffectsEvent ?: return false
        val expr = config.getStringOrNull("enchantment_override_level") ?: return false
        val levelStr = event.level.toString()
        val overrideLevelStr = event.overrideLevel.toString()

        event.overrideLevel = NumberUtils.evaluateExpression(expr
            .replace("%enchantment_level%", levelStr)
            .replace("%el%", levelStr)
            .replace("%enchantment_override_level%", overrideLevelStr)
            .replace("%eol%", overrideLevelStr),
            config.toPlaceholderContext(data)).toInt()

        return true
    }
}