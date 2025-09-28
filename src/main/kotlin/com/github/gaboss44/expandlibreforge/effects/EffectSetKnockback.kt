package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.toNiceString
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import io.papermc.paper.event.entity.EntityKnockbackEvent
import org.bukkit.util.Vector

object EffectSetKnockback : Effect<NoCompileData>("set_knockback") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityKnockbackEvent ?: return false
        val knockback = event.knockback
        val inputKnockback = knockback.clone()

        if (config.getBool("normalize_first")) knockback.normalize()

        config.getDoubleFromExpression(
            "multiply_first",
            data,
            knockback,
            inputKnockback
        )?.let { knockback.multiply(it) }

        config.getDoubleFromExpression(
            "x",
            data,
            knockback,
            inputKnockback
        )?.let { knockback.x = it }

        config.getDoubleFromExpression(
            "y",
            data,
            knockback,
            inputKnockback
        )?.let { knockback.y = it }

        config.getDoubleFromExpression(
            "z",
            data, knockback,
            inputKnockback
        )?.let { knockback.z = it }

        if (config.getBool("normalize_last")) knockback.normalize()

        config.getDoubleFromExpression(
            "multiply_last",
            data,
            knockback,
            inputKnockback
        )?.let { knockback.multiply(it) }

        event.knockback = knockback

        return true
    }
    
    private fun Config.getDoubleFromExpression(
        key: String,
        data: TriggerData,
        currentVector: Vector,
        inputVector: Vector? = null
    ): Double? {
        val string = this.getStringOrNull(key) ?: return null

        val currentX = currentVector.x.toNiceString()
        val currentY = currentVector.y.toNiceString()
        val currentZ = currentVector.z.toNiceString()
        val currentLength = currentVector.length().toNiceString()

        val inputX = inputVector?.x?.toNiceString() ?: currentX
        val inputY = inputVector?.y?.toNiceString() ?: currentY
        val inputZ = inputVector?.z?.toNiceString() ?: currentZ
        val inputLength = inputVector?.length()?.toNiceString() ?: currentLength

        val replaced = string
            .replace("%current_x%", currentX)
            .replace("%cx%", currentX)
            .replace("%current_y%", currentY)
            .replace("%cy%", currentY)
            .replace("%current_z%", currentZ)
            .replace("%cz%", currentZ)
            .replace("%current_length%", currentLength)
            .replace("%cl%", currentLength)
            .replace("%input_x%", inputX)
            .replace("%ix%", inputX)
            .replace("%input_y%", inputY)
            .replace("%iy%", inputY)
            .replace("%input_z%", inputZ)
            .replace("%iz%", inputZ)
            .replace("%input_length%", inputLength)
            .replace("%il%", inputLength)

        return NumberUtils.evaluateExpression(replaced, this.toPlaceholderContext(data))
    }
}