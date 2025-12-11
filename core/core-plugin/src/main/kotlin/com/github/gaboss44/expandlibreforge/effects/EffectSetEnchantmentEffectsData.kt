package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentEffectsEvent
import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsInSlotData
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData

object EffectSetEnchantmentEffectsData : Effect<NoCompileData>("set_enchantment_effects_data") {

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityEnchantmentEffectsEvent ?: return false
        val enchantmentEffectsData = event.enchantmentEffectsData

        val forceEffects = config.getBoolOrNull("force_effects")
        if (forceEffects != null) {
            enchantmentEffectsData.forceEffects.value = forceEffects
        }

        val cancelEffects = config.getBoolOrNull("cancel_effects")
        if (cancelEffects != null) {
            enchantmentEffectsData.cancelEffects.value = cancelEffects
        }

        val forceSlot = config.getBoolOrNull("force_slot")
        if (forceSlot != null && enchantmentEffectsData is EnchantmentEffectsInSlotData) {
            enchantmentEffectsData.forceSlot.value = forceSlot
        }

        val placeholderContext = config.toPlaceholderContext(data)

        val newLevel = config.getStringOrNull("new_level")?.let { expr ->
            val levelStr = enchantmentEffectsData.level.value.toString()
            val originalLevelStr = enchantmentEffectsData.originalLevel.toString()

            NumberUtils.evaluateExpression(expr
                .replace("%eed_level%", levelStr)
                .replace("%eed_original_level%", originalLevelStr),
                placeholderContext
            ).toInt()
        }
        if (newLevel != null) {
            enchantmentEffectsData.level.value = newLevel
        }

        return true
    }
}