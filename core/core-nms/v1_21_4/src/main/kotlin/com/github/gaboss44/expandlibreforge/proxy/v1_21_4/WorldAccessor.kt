package com.github.gaboss44.expandlibreforge.proxy.v1_21_4

import com.github.gaboss44.expandlibreforge.proxies.WorldAccessorProxy
import com.willfp.eco.core.Prerequisite
import org.bukkit.World
import org.bukkit.craftbukkit.CraftWorld

class WorldAccessor : WorldAccessorProxy {

    override fun arePlayerCritsDisabled(world: World): Boolean {
        if (!Prerequisite.HAS_PAPER.isMet) return false
        val nmsLevel = (world as CraftWorld).handle!!
        return nmsLevel.paperConfig().entities.behavior.disablePlayerCrits
    }
}