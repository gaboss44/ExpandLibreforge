package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentEffectsEvent
import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsInSlotData
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.getStrings
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData

object FilterEnchantmentEffectsData : Filter<NoCompileData, Config>("filter_enchantment_effects_data") {
    override fun getValue(
        config: Config,
        data: TriggerData?,
        key: String
    ): Config {
        return config.getSubsection(key)
    }

    override fun isMet(
        data: TriggerData,
        value: Config,
        compileData: NoCompileData
    ): Boolean {
        val event = data.event as? EntityEnchantmentEffectsEvent ?: return true
        val enchantmentEffectsData = event.enchantmentEffectsData
        val config = value

        config.getBoolOrNull("forces_effects")?.let { forcesEffects ->
            if (enchantmentEffectsData.forceEffects.value != forcesEffects) {
                return false
            }
        }

        config.getBoolOrNull("cancels_effects")?.let { cancelsEffects ->
            if (enchantmentEffectsData.cancelEffects.value != cancelsEffects) {
                return false
            }
        }

        config.getBoolOrNull("forces_slot")?.let { forcesSlot ->
            if (enchantmentEffectsData is EnchantmentEffectsInSlotData && enchantmentEffectsData.forceSlot.value != forcesSlot) {
                return false
            }
        }

        val enchantments = config.getStrings("enchantments", "enchantment").filter(CharSequence::isNotBlank)
        if (enchantments.isNotEmpty() && !enchantments.containsIgnoreCase(enchantmentEffectsData.enchantment.key.key)) {
            return false
        }

        val slots = config.getStrings("slots", "slot").filter(CharSequence::isNotBlank)
        if (slots.isNotEmpty() && enchantmentEffectsData is EnchantmentEffectsInSlotData && !slots.containsIgnoreCase(enchantmentEffectsData.slot.name)) {
            return false
        }

        val placeholderContext = config.toPlaceholderContext(data)

        val expressions = config.getStrings("expressions", "expression").filter(CharSequence::isNotBlank)
        if (expressions.isNotEmpty()) {
            val levelStr = enchantmentEffectsData.level.value.toString()
            val originalLevelStr = enchantmentEffectsData.originalLevel.toString()
            for (expression in expressions) {
                val evaluated = NumberUtils.evaluateExpression(expression
                    .replace("%eed_level%", levelStr)
                    .replace("%eed_original_level%", originalLevelStr),
                    placeholderContext
                )
                if (evaluated != 1.0) {
                    return false
                }
            }
        }

        return true
    }
}