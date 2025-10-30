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
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

object EffectRejectDamageTakenByItemUsingPoisson :
    Effect<NoCompileData>("reject_damage_taken_by_item_using_poisson") {

    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("rejection_segment", "You must define a rejection segment")
        require("rejection_chance", "You must define a rejection chance")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? PlayerItemDamageEvent ?: return false
        val damage = event.damage.toDouble()
        if (damage <= 0) return false

        val item = event.item
        if (config.getBool("require_holder") && item != data.holder.getProvider<ItemStack>()) return false

        val placeholderContext = config.toPlaceholderContext(data)

        // --- Resolve expressions ---
        fun Config.expr(key: String, vararg replacements: Pair<String, String>): Double {
            var expr = this.getString(key)
            replacements.forEach { (k, v) -> expr = expr.replace(k, v) }
            return NumberUtils.evaluateExpression(expr, placeholderContext)
        }

        val damageStr = damage.toString()
        val itemDamageStr = item.durability.toString()
        val itemMaxDamageStr = item.type.maxDurability.toString()

        val segment = config.expr("rejection_segment",
            "%damage%" to damageStr,
            "%item_damage%" to itemDamageStr,
            "%item_max_damage%" to itemMaxDamageStr,
            "%d%" to damageStr,
            "%itd%" to itemDamageStr,
            "%imd%" to itemMaxDamageStr
        ).coerceAtLeast(0.1) // Prevent zero or negative segment

        val chance = config.expr("rejection_chance",
            "%damage%" to damageStr,
            "%item_damage%" to itemDamageStr,
            "%item_max_damage%" to itemMaxDamageStr,
            "%d%" to damageStr,
            "%itd%" to itemDamageStr,
            "%imd%" to itemMaxDamageStr
        ).coerceIn(0.0, 100.0) / 100.0 // Apply as a probability factor

        val maxRejection = if (config.has("max_rejection")) config.expr("max_rejection",
            "%damage%" to damageStr,
            "%item_damage%" to itemDamageStr,
            "%item_max_damage%" to itemMaxDamageStr,
            "%d%" to damageStr,
            "%itd%" to itemDamageStr,
            "%imd%" to itemMaxDamageStr) else damage

        // --- Compute number of segments based on maxRejection, not total damage ---
        val parts = floor(maxRejection / segment).toInt()
        if (parts <= 0 || chance <= 0.0) return false

        // --- Expected rejection = parts * p, compressed into single-random Poisson-like sample ---
        val expected = parts * chance
        val random = NumberUtils.randFloat(0.0, 1.0)

        // Using exponential decay to simulate bursty binomial variance with only ONE random call.
        // Explanation: rejection ~ Poisson(λ=expected) approximation using inverse transform.
        val rejection = floor(-kotlin.math.ln(1 - random) * expected)

        // Clamp to maxRejection and originalDamage
        val finalRejection = min(rejection * segment, maxRejection).coerceAtMost(damage)

        // Apply final damage after all calculations
        val newDamage = max(damage - finalRejection, 0.0)
        event.damage = newDamage.toInt()

        // Silent return, no debug/log
        return true
    }
}
