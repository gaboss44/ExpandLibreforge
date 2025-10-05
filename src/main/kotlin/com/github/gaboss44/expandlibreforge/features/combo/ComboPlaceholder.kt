package com.github.gaboss44.expandlibreforge.features.combo

import com.github.gaboss44.expandlibreforge.ExpandLibreforgePlugin
import com.github.gaboss44.expandlibreforge.util.KRegistrablePlaceholder
import com.willfp.eco.core.placeholder.context.PlaceholderContext
import java.util.regex.Pattern

class ComboPlaceholder private constructor(
    plugin: ExpandLibreforgePlugin,
    pattern: Pattern,
    function: (args: String, ctx: PlaceholderContext) -> String?
) : KRegistrablePlaceholder(
    plugin = plugin,
    pattern = pattern,
    function = function
) {
    override fun getValue(args: String, context: PlaceholderContext): String? {
        return super.getValue(args, context)
    }

    companion object {

        private val functionHas = functionHas@ { args: String, ctx: PlaceholderContext ->
            val player = ctx.player ?: return@functionHas null
            val combos = ComboManager.getCombos(player.uniqueId) ?: return@functionHas "false"
            if (combos.contains(args.replaceFirst("combo_has_", ""))) "true" else "false"
        }

        private val patternHas = Pattern.compile("^combo_has_(\\S+)$")

        fun createHas(plugin: ExpandLibreforgePlugin) = ComboPlaceholder(
            plugin = plugin,
            pattern = patternHas,
            function = functionHas
        )

        private val functionCount = functionCount@ { args: String, ctx: PlaceholderContext ->
            val player = ctx.player ?: return@functionCount null
            val combos = ComboManager.getCombos(player.uniqueId) ?: return@functionCount "0"
            val combo = combos[args.replaceFirst("combo_count_", "")] ?: return@functionCount "0"
            combo.count.toString()
        }

        private val patternCount = Pattern.compile("^combo_count_(\\S+)$")

        fun createCount(plugin: ExpandLibreforgePlugin) = ComboPlaceholder(
            plugin = plugin,
            pattern = patternCount,
            function = functionCount
        )

        private val functionRemainingTicks = functionRemainingTicks@ { args: String, ctx: PlaceholderContext ->
            val player = ctx.player ?: return@functionRemainingTicks null
            val combos = ComboManager.getCombos(player.uniqueId) ?: return@functionRemainingTicks "0"
            val combo = combos[args.replaceFirst("combo_remainingticks_", "")] ?: return@functionRemainingTicks "0"
            combo.remainingTicks.toString()
        }

        private val patternRemainingTicks = Pattern.compile("^combo_remainingticks_(\\S+)$")

        fun createRemainingTicks(plugin: ExpandLibreforgePlugin) = ComboPlaceholder(
            plugin = plugin,
            pattern = patternRemainingTicks,
            function = functionRemainingTicks
        )

        private val functionScore = functionScore@ { args: String, ctx: PlaceholderContext ->
            val player = ctx.player ?: return@functionScore null
            val combos = ComboManager.getCombos(player.uniqueId) ?: return@functionScore "0"
            val combo = combos[args.replaceFirst("combo_score_", "")] ?: return@functionScore "0"
            combo.score.toString()
        }

        private val patternScore = Pattern.compile("^combo_score_(\\S+)$")

        fun createScore(plugin: ExpandLibreforgePlugin) = ComboPlaceholder(
            plugin = plugin,
            pattern = patternScore,
            function = functionScore
        )
    }
}