package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.extensions.reformulateDamageModifiers
import com.github.gaboss44.expandlibreforge.extensions.toDamageModifier
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getStrings
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.entity.EntityDamageEvent

object EffectReformulateDamageModifiers : Effect<NoCompileData>("reformulate_damage_modifiers") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require(listOf("damage_modifier, damage_modifiers"), "You must specify one or more damage modifiers to reformulate")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDamageEvent ?: return false
        val modifiers = config.getStrings("damage_modifiers", "damage_modifier").mapNotNull(String::toDamageModifier)
        event.reformulateDamageModifiers(config.getBool("skip_recalculation"), modifiers)
        return true
    }
}