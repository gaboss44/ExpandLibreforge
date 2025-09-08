package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.SoundUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.getFormattedString
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.SoundCategory
import kotlin.text.uppercase

object EffectPlaySoundKey : Effect<NoCompileData>("play_sound_key") {
    override val parameters = setOf(
        TriggerParameter.PLAYER
    )

    override val arguments = arguments {
        require("sound", "You must specify the sound key to play")
        require("pitch", "You must specify the sound pitch")
        require("volume", "You must specify the sound volume")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false

        val location = player.location
        val off_x = config.getDoubleFromExpression("off_x", data)
        val off_y = config.getDoubleFromExpression("off_y", data)
        val off_z = config.getDoubleFromExpression("off_z", data)
        location.add(off_x, off_y, off_z)

        val sound = config.getFormattedString("sound", data)
        val pitch = config.getDoubleFromExpression("pitch", data)
        val volume = config.getDoubleFromExpression("volume", data)

        val category = getSoundCategoryOrElse(
            config.getStringOrNull("category"),
            SoundCategory.MASTER
        )

        if (Prerequisite.HAS_1_20_3.isMet && config.has("seed")) {
            val seed = config.getDoubleFromExpression("seed", data).toLong()
            player.playSound(location, sound, category, volume.toFloat(), pitch.toFloat(), seed)
        }

        else player.playSound(location, sound, category, volume.toFloat(), pitch.toFloat())

        return true
    }

    fun getSoundCategoryOrElse(string: String?, default: SoundCategory): SoundCategory {
        return try {
            if (string == null) {
                default
            } else {
                SoundCategory.valueOf(string.uppercase())
            }
        } catch (_: IllegalArgumentException) {
            default
        }
    }
}