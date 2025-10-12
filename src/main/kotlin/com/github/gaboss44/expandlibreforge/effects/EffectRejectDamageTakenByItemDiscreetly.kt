package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getProvider
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.min

object EffectRejectDamageTakenByItemDiscreetly :
    Effect<NoCompileData>("reject_damage_taken_by_item_discreetly") {

    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("step", "You must define a step")
        require("bound", "You must define a bound")
        require("threshold", "You must define a threshold")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? PlayerItemDamageEvent ?: return false
        val damage = event.damage
        if (damage <= 0) return false

        val item = event.item
        if (config.getBool("require_holder") && item != data.holder.getProvider<ItemStack>()) return false

        val placeholderContext = config.toPlaceholderContext(data)

        fun Config.exprInt(key: String, vararg replacements: Pair<String, String>): Int {
            var expr = this.getString(key)
            replacements.forEach { (k, v) -> expr = expr.replace(k, v) }
            return NumberUtils.evaluateExpression(expr, placeholderContext).toInt()
        }

        val damageStr = damage.toString()
        val itemDamageStr = item.durability.toString()
        val itemMaxDamageStr = item.type.maxDurability.toString()

        val step = config.exprInt("step",
            "%damage%" to damageStr,
            "%item_damage%" to itemDamageStr,
            "%item_max_damage%" to itemMaxDamageStr,
            "%d%" to damageStr,
            "%itd%" to itemDamageStr,
            "%imd%" to itemMaxDamageStr).coerceIn(1, damage)

        val bound = config.exprInt("bound",
            "%damage%" to damageStr,
            "%item_damage%" to itemDamageStr,
            "%item_max_damage%" to itemMaxDamageStr,
            "%d%" to damageStr,
            "%itd%" to itemDamageStr,
            "%imd%" to itemMaxDamageStr).coerceAtLeast(1)

        val threshold = config.exprInt("threshold",
            "%damage%" to damageStr,
            "%item_damage%" to itemDamageStr,
            "%item_max_damage%" to itemMaxDamageStr,
            "%d%" to damageStr,
            "%itd%" to itemDamageStr,
            "%imd%" to itemMaxDamageStr)

        val applyResidual = config.getBool("apply_residual_step")

        val maxRejection = if (config.has("max_rejection")) {
            config.exprInt("max_rejection",
                "%damage%" to damageStr,
                "%item_damage%" to itemDamageStr,
                "%item_max_damage%" to itemMaxDamageStr,
                "%d%" to damageStr,
                "%itd%" to itemDamageStr,
                "%imd%" to itemMaxDamageStr).coerceIn(0, damage)
        } else {
            damage
        }

        val fullIterations = damage / step
        val residual = damage % step

        var totalRejected = 0

        repeat (fullIterations) {
            val r = NumberUtils.randInt(0, bound) + 1
            if (r >= threshold) {
                totalRejected += step
                if (totalRejected >= maxRejection) return@repeat
            }
        }

        // residual step if allowed
        if (applyResidual && totalRejected < maxRejection && residual > 0) {
            val r = NumberUtils.randInt(0, bound)
            if (r >= threshold) {
                totalRejected += residual
            }
        }

        totalRejected = min(totalRejected, min(maxRejection, damage))
        event.damage = (damage - totalRejected).coerceAtLeast(0)

        return true
    }
}
