package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.util.EntityTarget
import com.github.gaboss44.expandlibreforge.util.getSoundCategoryOrElse
import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.toNiceString
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.getFormattedString
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.SoundCategory
import org.bukkit.entity.Player

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
        val target = EntityTarget[config.getString("target")]?.getEntity(data) as? Player ?: data.player ?: return false

        val targetLocation = target.location

        val center = EntityTarget[config.getString("center")]?.getEntity(data)

        val centerLocation = center?.location ?: data.location?.clone() ?: targetLocation

        if (config.getBool("require_same_world") && targetLocation.world != centerLocation.world) return false

        val offX = config.getDoubleFromExpression("off_x", data)
        val offY = config.getDoubleFromExpression("off_y", data)
        val offZ = config.getDoubleFromExpression("off_z", data)
        centerLocation.add(offX, offY, offZ)

        val sound = config.getFormattedString("sound", data)
        val pitch = config.getDoubleFromExpression("pitch", data)
        val volume = config.getDoubleFromExpression("volume", data)

        val category = getSoundCategoryOrElse(
            config.getStringOrNull("category"),
            SoundCategory.MASTER
        )

        val volumeMultiplier = config.getStringOrNull("volume_multiplier")
            ?.takeUnless { it.isBlank() }
            ?.let { multiplierStr ->
                val targetXStr = targetLocation.x.toNiceString()
                val targetYStr = targetLocation.y.toNiceString()
                val targetZStr = targetLocation.z.toNiceString()

                val centerXStr = centerLocation.x.toNiceString()
                val centerYStr = centerLocation.y.toNiceString()
                val centerZStr = centerLocation.z.toNiceString()

                val distanceStr = targetLocation.toVector().distanceSquared(centerLocation.toVector()).toFloat().toString()

                val expr = multiplierStr
                    .replace("%target_x%", targetXStr)
                    .replace("%target_y%", targetYStr)
                    .replace("%target_z%", targetZStr)
                    .replace("%center_x%", centerXStr)
                    .replace("%center_y%", centerYStr)
                    .replace("%center_z%", centerZStr)
                    .replace("%distance%", distanceStr)
                    .replace("%tx%", targetXStr)
                    .replace("%ty%", targetYStr)
                    .replace("%tz%", targetZStr)
                    .replace("%cx%", centerXStr)
                    .replace("%cy%", centerYStr)
                    .replace("%cz%", centerZStr)
                    .replace("%d%", distanceStr)

                NumberUtils.evaluateExpression(expr, config.toPlaceholderContext(data)).toFloat()
            } ?: 1.0f

        if (Prerequisite.HAS_1_20_3.isMet && config.has("seed")) {
            val seed = config.getDoubleFromExpression("seed", data).toLong()
            target.playSound(centerLocation, sound, category, volume.toFloat() * volumeMultiplier, pitch.toFloat(), seed)
        }

        else target.playSound(centerLocation, sound, category, volume.toFloat() * volumeMultiplier, pitch.toFloat())

        return true
    }
}