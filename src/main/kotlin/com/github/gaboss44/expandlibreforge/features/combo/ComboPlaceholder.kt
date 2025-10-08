package com.github.gaboss44.expandlibreforge.features.combo

import com.github.gaboss44.expandlibreforge.ExpandLibreforgePlugin
import com.github.gaboss44.expandlibreforge.util.KRegistrablePlaceholder
import com.willfp.eco.core.placeholder.context.PlaceholderContext
import com.willfp.eco.util.toNiceString
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
            combo.count.toNiceString()
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
            combo.remainingTicks.toNiceString()
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
            combo.score.toNiceString()
        }

        private val patternScore = Pattern.compile("^combo_score_(\\S+)$")

        fun createScore(plugin: ExpandLibreforgePlugin) = ComboPlaceholder(
            plugin = plugin,
            pattern = patternScore,
            function = functionScore
        )

        private val functionMaximumTicks = functionMaximumTicks@ { args: String, ctx: PlaceholderContext ->
            val player = ctx.player ?: return@functionMaximumTicks null
            val combos = ComboManager.getCombos(player.uniqueId) ?: return@functionMaximumTicks "0"
            val combo = combos[args.replaceFirst("combo_maximumticks_", "")] ?: return@functionMaximumTicks "0"
            combo.maximumTicks.toNiceString()
        }

        private val patternMaximumTicks = Pattern.compile("^combo_maximumticks_(\\S+)$")

        fun createMaximumTicks(plugin: ExpandLibreforgePlugin) = ComboPlaceholder(
            plugin = plugin,
            pattern = patternMaximumTicks,
            function = functionMaximumTicks
        )

        private val functionInitialTicks = functionInitialTicks@ { args: String, ctx: PlaceholderContext ->
            val player = ctx.player ?: return@functionInitialTicks null
            val combos = ComboManager.getCombos(player.uniqueId) ?: return@functionInitialTicks "0"
            val combo = combos[args.replaceFirst("combo_initialticks_", "")] ?: return@functionInitialTicks "0"
            combo.initialTicks.toNiceString()
        }

        private val patternInitialTicks = Pattern.compile("^combo_initialticks_(\\S+)$")

        fun createInitialTicks(plugin: ExpandLibreforgePlugin) = ComboPlaceholder(
            plugin = plugin,
            pattern = patternInitialTicks,
            function = functionInitialTicks
        )

        private val functionCompletedTicks = functionCompletedTicks@ { args: String, ctx: PlaceholderContext ->
            val player = ctx.player ?: return@functionCompletedTicks null
            val combos = ComboManager.getCombos(player.uniqueId) ?: return@functionCompletedTicks "0"
            val combo = combos[args.replaceFirst("combo_completedticks_", "")] ?: return@functionCompletedTicks "0"
            combo.completedTicks.toNiceString()
        }

        private val patternCompletedTicks = Pattern.compile("^combo_completedticks_(\\S+)$")

        fun createCompletedTicks(plugin: ExpandLibreforgePlugin) = ComboPlaceholder(
            plugin = plugin,
            pattern = patternCompletedTicks,
            function = functionCompletedTicks
        )
    }
}