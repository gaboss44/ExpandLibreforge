package com.github.gaboss44.expandlibreforge.integrations

import com.github.gaboss44.expandlibreforge.effects.*
import com.github.gaboss44.expandlibreforge.filters.*
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

        Effects.register(EffectSetReviveHealth)

        Filters.register(FilterReviveHealth.AtLeast)
        Filters.register(FilterReviveHealth.AtMost)
        Filters.register(FilterReviveHealth.Equals)
        Filters.register(FilterReviveHealth.GreaterThan)
        Filters.register(FilterReviveHealth.LowerThan)

        Filters.register(FilterDeathSound)
        Filters.register(FilterHasDeathSound)
        Effects.register(EffectSetDeathSound)

        Filters.register(FilterBeaconEffect)

        Filters.register(FilterCritical)
    }

    override fun getPluginName() = "Paper"
}