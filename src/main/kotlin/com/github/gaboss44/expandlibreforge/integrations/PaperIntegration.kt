package com.github.gaboss44.expandlibreforge.integrations

import com.github.gaboss44.expandlibreforge.effects.*
import com.github.gaboss44.expandlibreforge.filters.*
import com.github.gaboss44.expandlibreforge.triggers.TriggerDisableShield
import com.github.gaboss44.expandlibreforge.triggers.TriggerInflictKnockback
import com.github.gaboss44.expandlibreforge.triggers.TriggerLocalMove
import com.github.gaboss44.expandlibreforge.triggers.TriggerServerTickEnd
import com.github.gaboss44.expandlibreforge.triggers.TriggerServerTickStart
import com.github.gaboss44.expandlibreforge.triggers.TriggerShieldDisable
import com.github.gaboss44.expandlibreforge.triggers.TriggerTakeKnockback
import com.github.gaboss44.expandlibreforge.util.MethodUtils
import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.util.ClassUtils
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.integrations.LoadableIntegration
import com.willfp.libreforge.triggers.Triggers
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

        Effects.register(EffectSetShieldDisableCooldown)

        TriggerShieldDisable.registerAllInto(Triggers)
        TriggerDisableShield.registerAllInto(Triggers)

        TriggerLocalMove.registerAllInto(Triggers)

        if (ClassUtils.exists("io.papermc.paper.event.entity.EntityKnockbackEvent")) {
            TriggerTakeKnockback.registerAllInto(Triggers)
            TriggerInflictKnockback.registerAllInto(Triggers)

            Effects.register(EffectSetKnockback)

            Filters.register(FilterKnockerIsPresent)
        }

        Triggers.register(TriggerServerTickStart)
        Triggers.register(TriggerServerTickEnd)

        FilterReviveHealth.registerAllInto(Filters)

        Filters.register(FilterDeathSound)
        Filters.register(FilterHasDeathSound)
        Effects.register(EffectSetDeathSound)

        Filters.register(FilterBeaconEffect)

        Filters.register(FilterCritical)
    }

    override fun getPluginName() = "Paper"
}