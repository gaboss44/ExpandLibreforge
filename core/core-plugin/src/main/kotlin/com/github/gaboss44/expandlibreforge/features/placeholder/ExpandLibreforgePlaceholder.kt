package com.github.gaboss44.expandlibreforge.features.placeholder

import com.github.gaboss44.expandlibreforge.ExpandLibreforgePlugin
import com.github.gaboss44.expandlibreforge.util.KRegistrablePlaceholder
import com.willfp.eco.core.placeholder.context.PlaceholderContext
import com.willfp.eco.util.PatternUtils
import java.util.regex.Pattern

class ExpandLibreforgePlaceholder private constructor(
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

        private val patternFallDistance = PatternUtils.compileLiteral("fall_distance")

        private val functionFallDistance = functionFallDistance@ { args: String, ctx: PlaceholderContext ->
            val player = ctx.player ?: return@functionFallDistance null
            player.fallDistance.toString()
        }

        fun createFallDistance(plugin: ExpandLibreforgePlugin) = ExpandLibreforgePlaceholder(
            plugin,
            patternFallDistance,
            functionFallDistance
        )
    }
}