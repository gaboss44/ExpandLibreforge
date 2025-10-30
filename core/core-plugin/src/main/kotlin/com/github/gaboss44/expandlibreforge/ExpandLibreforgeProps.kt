package com.github.gaboss44.expandlibreforge

object ExpandLibreforgeProps {

    lateinit var plugin: ExpandLibreforgePlugin private set

    internal fun register(plugin: ExpandLibreforgePlugin) {
        this.plugin = plugin
    }
}