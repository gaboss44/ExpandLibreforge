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
import org.bukkit.Bukkit
import org.bukkit.SoundCategory

object EffectPlaySoundKeyToWorld : Effect<NoCompileData>("play_sound_key_to_world") {
    override val parameters = setOf(
        TriggerParameter.LOCATION
    )

    override val arguments = arguments {
        require("sound", "You must specify the sound key to play")
        require("pitch", "You must specify the sound pitch")
        require("volume", "You must specify the sound volume")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val location = data.location?.clone() ?: return false

        val worldStr = if (config.has("world")) config.getFormattedString("world", data) else null

        val world = worldStr?.let { Bukkit.getWorld(worldStr) } ?: location.world

        val center = EntityTarget[config.getString("center")]?.getEntity(data)

        val centerLocation = center?.location ?: location

        val offX = config.getDoubleFromExpression("off_x", data)
        val offY = config.getDoubleFromExpression("off_y", data)
        val offZ = config.getDoubleFromExpression("off_z", data)
        location.add(offX, offY, offZ)

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
                val centerXStr = centerLocation.x.toNiceString()
                val centerYStr = centerLocation.y.toNiceString()
                val centerZStr = centerLocation.z.toNiceString()

                val locationXStr = location.x.toNiceString()
                val locationYStr = location.y.toNiceString()
                val locationZStr = location.z.toNiceString()

                val distanceStr = centerLocation.toVector().distanceSquared(location.toVector()).toFloat().toString()

                val expr = multiplierStr
                    .replace("%center_x%", centerXStr)
                    .replace("%center_y%", centerYStr)
                    .replace("%center_z%", centerZStr)
                    .replace("%location_x%", locationXStr)
                    .replace("%location_y%", locationYStr)
                    .replace("%location_z%", locationZStr)
                    .replace("%distance%", distanceStr)
                    .replace("%cx%", centerXStr)
                    .replace("%cy%", centerYStr)
                    .replace("%cz%", centerZStr)
                    .replace("%lx%", locationXStr)
                    .replace("%ly%", locationYStr)
                    .replace("%lz%", locationZStr)
                    .replace("%d%", distanceStr)

                NumberUtils.evaluateExpression(expr, config.toPlaceholderContext(data)).toFloat()
            } ?: 1.0f

        if (Prerequisite.HAS_1_20_3.isMet && config.has("seed")) {
            val seed = config.getDoubleFromExpression("seed", data).toLong()
            world.playSound(centerLocation, sound, category, volume.toFloat() * volumeMultiplier, pitch.toFloat(), seed)
        }

        else world.playSound(centerLocation, sound, category, volume.toFloat() * volumeMultiplier, pitch.toFloat())

        return true
    }
}