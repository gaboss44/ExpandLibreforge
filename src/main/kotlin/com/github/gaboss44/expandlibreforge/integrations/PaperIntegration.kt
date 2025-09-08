package com.github.gaboss44.expandlibreforge.integrations

import com.github.gaboss44.expandlibreforge.effects.EffectDoRiptideSpin
import com.github.gaboss44.expandlibreforge.filters.FilterBeaconEffect
import com.github.gaboss44.expandlibreforge.filters.FilterCritical
import com.github.gaboss44.expandlibreforge.util.MethodUtils
import com.willfp.eco.core.EcoPlugin
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.integrations.LoadableIntegration
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.ItemStack

object PaperIntegration : LoadableIntegration {
    override fun load(plugin: EcoPlugin) {
        if (MethodUtils.hasMethod(
                HumanEntity::class.java,
                "startRiptideAttack",
                Int::class.javaPrimitiveType!!,
                Float::class.javaPrimitiveType!!,
                ItemStack::class.java
            )) {
            Effects.register(EffectDoRiptideSpin)
        }
        Filters.register(FilterBeaconEffect)
        Filters.register(FilterCritical)
    }

    override fun getPluginName() = "Paper"
}