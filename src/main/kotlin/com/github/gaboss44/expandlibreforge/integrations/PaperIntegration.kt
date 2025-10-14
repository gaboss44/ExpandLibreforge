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
import org.bukkit.entity.Entity
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
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

        TriggerShieldDisable.registerAllInto(Triggers)
        TriggerDisableShield.registerAllInto(Triggers)

        TriggerLocalMove.registerAllInto(Triggers)

        if (ClassUtils.exists("io.papermc.paper.event.entity.EntityKnockbackEvent")) {
            TriggerTakeKnockback.registerAllInto(Triggers)
            TriggerInflictKnockback.registerAllInto(Triggers)

            Effects.register(EffectSetKnockback)

            Filters.register(FilterKnockerIsPresent)
        }

        if (ClassUtils.exists("io.papermc.paper.event.entity.EntityBlockingDelayCheckEvent")) {
            Triggers.register(TriggerBlockingCheck)
            Effects.register(EffectSetBlockingDelay)
        }

        if (ClassUtils.exists("io.papermc.paper.event.player.PlayerAttackEntityCriticalCheckEvent")) {
            TriggerAttackCriticalCheck.registerAllInto(Triggers)

            Filters.register(FilterWasOriginallyCritical)
            Filters.register(FilterIsCritical)

            FilterCriticalMultiplier.registerAllInto(Filters)
            FilterOriginalCriticalMultiplier.registerAllInto(Filters)

            Effects.register(EffectSetCriticalAttack)
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

        if (ClassUtils.exists("io.papermc.paper.event.entity.EntityAttemptSmashAttackEvent")) {
            TriggerSmashAttempt.registerAllInto(Triggers)

            Effects.register(EffectSetSmashAttemptResult)

            Filters.register(FilterSmashAttemptIsOriginallySuccessful)
            Filters.register(FilterMatchSmashAttemptResultIfPresent)
            Filters.register(FilterIgnoreSmashAttemptResultIfPresent)
        }

        if (MethodUtils.hasMethod(
                LivingEntity::class.java,
                "attack",
                Entity::class.java,
                Boolean::class.javaPrimitiveType!!)) {
            Effects.register(EffectAttackVictimWithOffhand)
        }

        if (MethodUtils.hasMethod(
                Player::class.java,
                "canInteractWithEntity",
                Entity::class.java,
                Double::class.javaPrimitiveType!!)) {
            Filters.register(FilterEntityInteractivity)
        }

        if (MethodUtils.hasMethod(
                Player::class.java,
                "setAttackStrengthTicker",
                Int::class.javaPrimitiveType!!)) {
            Effects.register(EffectSetAttackStrengthTicker)
        }

        if (MethodUtils.hasMethod(
                Player::class.java,
                "getAttackStrengthTicker")) {
            FilterAttackStrengthTicker.registerAllInto(Filters)
            FilterVictimAttackStrengthTicker.registerAllInto(Filters)
        }

        if (ClassUtils.exists("io.papermc.paper.event.entity.EntityCanSmashAttackCheckEvent")) {
            TriggerSmashCheck.registerAllInto(Triggers)

            Filters.register(FilterMatchSmashCheckReasonIfPresent)
            Filters.register(FilterMatchSmashCheckResultIfPresent)
            Filters.register(FilterIgnoreSmashCheckReasonIfPresent)
            Filters.register(FilterIgnoreSmashCheckResultIfPresent)
        }
    }

    override fun getPluginName() = "Paper"
}