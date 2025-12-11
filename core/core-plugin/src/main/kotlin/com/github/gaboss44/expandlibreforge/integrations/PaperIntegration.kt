package com.github.gaboss44.expandlibreforge.integrations

import com.github.gaboss44.expandlibreforge.effects.*
import com.github.gaboss44.expandlibreforge.filters.*
import com.github.gaboss44.expandlibreforge.triggers.*
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

        Effects.register(EffectResetCurrentCooldownPeriod)

        Effects.register(EffectOverrideAttack)

        TriggerShieldDisable.registerAllInto(Triggers)
        TriggerDisableShield.registerAllInto(Triggers)

        TriggerLocalMove.registerAllInto(Triggers)

        TriggerPrePlayerAttack.registerAllInto(Triggers)

        if (ClassUtils.exists("io.papermc.paper.events.entity.EntityKnockbackEvent")) {
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
        Filters.register(FilterSprintingOrCritical)

        FilterPlayerCooledAttackStrength.registerAllInto(Filters)
        FilterVictimCooledAttackStrength.registerAllInto(Filters)

        FilterPlayerCurrentCooldownPeriod.registerAllInto(Filters)
        FilterVictimCurrentCooldownPeriod.registerAllInto(Filters)

        Effects.register(EffectSetSmashAttackAttemptResult)
        Effects.register(EffectSetCriticalAttackAttemptResult)

        Filters.register(FilterSmashAttackAttemptIsOriginallySuccessful)
        Filters.register(FilterCriticalAttackAttemptIsOriginallySuccessful)
        Filters.register(FilterMatchSmashAttackAttemptResultIfPresent)
        Filters.register(FilterMatchCriticalAttackAttemptResultIfPresent)
        Filters.register(FilterIgnoreSmashAttackAttemptResultIfPresent)
        Filters.register(FilterIgnoreCriticalAttackAttemptResultIfPresent)
    }

    override fun getPluginName() = "Paper"
}