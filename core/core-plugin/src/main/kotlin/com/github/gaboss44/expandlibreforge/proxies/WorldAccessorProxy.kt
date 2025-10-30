package com.github.gaboss44.expandlibreforge.proxies

import org.bukkit.World

interface WorldAccessorProxy {

    fun arePlayerCritsDisabled(world: World): Boolean
}