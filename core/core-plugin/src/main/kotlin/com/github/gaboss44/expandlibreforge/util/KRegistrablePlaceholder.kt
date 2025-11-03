package com.github.gaboss44.expandlibreforge.util

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.placeholder.RegistrablePlaceholder
import com.willfp.eco.core.placeholder.context.PlaceholderContext
import java.util.regex.Pattern

abstract class KRegistrablePlaceholder(
    private var plugin: EcoPlugin,
    private var pattern: Pattern,
    private var function: (args: String, context: PlaceholderContext) -> String?
) : RegistrablePlaceholder {
    override fun getPlugin() = plugin

    override fun getValue(
        args: String,
        context: PlaceholderContext
    ) = function(args, context)

    override fun getPattern() = pattern
}