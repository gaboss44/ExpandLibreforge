package com.github.gaboss44.expandlibreforge.extensions

import com.github.gaboss44.expandlibreforge.proxies.WorldAccessorProxy
import org.bukkit.World

private lateinit var worldAccessor: WorldAccessorProxy

object WorldExtensions {

    fun setProxyIfNeeded(proxy: WorldAccessorProxy) {
        if (!::worldAccessor.isInitialized) {
            worldAccessor = proxy
        }
    }
}

val World.arePlayerCritsDisabled get() = worldAccessor.arePlayerCritsDisabled(this)