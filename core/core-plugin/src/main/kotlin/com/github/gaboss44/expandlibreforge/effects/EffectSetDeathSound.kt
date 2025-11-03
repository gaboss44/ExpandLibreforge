package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.util.SoundCategoryUtils
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.SoundUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.entity.EntityDeathEvent

object EffectSetDeathSound : Effect<NoCompileData>("set_death_sound") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("sound", "You must specify a sound")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDeathEvent ?: return false

        SoundUtils.getSound(config.getString("sound"))?.let {
            event.deathSound = it
        }

        SoundCategoryUtils.getCategoryOrElse(config.getStringOrNull("category"), null)?.let {
            event.deathSoundCategory = it
        }

        config.getStringOrNull("volume")?.let {
            event.deathSoundVolume = NumberUtils.evaluateExpression(
                it, config.toPlaceholderContext(data)).toFloat()
        }

        config.getStringOrNull("pitch")?.let {
            event.deathSoundPitch = NumberUtils.evaluateExpression(
                it, config.toPlaceholderContext(data)).toFloat()
        }

        return true
    }
}