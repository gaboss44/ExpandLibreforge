package com.github.gaboss44.expandlibreforge

import com.github.gaboss44.expandlibreforge.conditions.ConditionHasCombo
import com.github.gaboss44.expandlibreforge.conditions.ConditionPlayerCurrentInput
import com.github.gaboss44.expandlibreforge.effects.*
import com.github.gaboss44.expandlibreforge.features.combo.ComboPlaceholder
import com.github.gaboss44.expandlibreforge.filters.*
import com.github.gaboss44.expandlibreforge.triggers.*
import com.github.gaboss44.expandlibreforge.mutators.*
import com.github.gaboss44.expandlibreforge.integrations.*
import com.github.gaboss44.expandlibreforge.listeners.DamageListener
import com.github.gaboss44.expandlibreforge.listeners.PlayerInputListener
import com.github.gaboss44.expandlibreforge.listeners.PlayerJoinListener
import com.github.gaboss44.expandlibreforge.listeners.PlayerQuitListener
import com.github.gaboss44.expandlibreforge.listeners.ServerTickListener
import com.willfp.eco.core.Prerequisite
import com.willfp.eco.util.ClassUtils
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.mutators.Mutators
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.event.Listener

class ExpandLibreforgePlugin : LibreforgePlugin() {

    init {
        ExpandLibreforgeProps.register(this)
    }

    override fun handleEnable() {

        Effects.register(EffectPlaySoundKey)

        Effects.register(EffectSetGravity)
        Effects.register(EffectSetSilent)

        Effects.register(EffectSetNoDamageTicks)

        Effects.register(EffectAntigravity)
        Effects.register(EffectInvulnerability)
        Effects.register(EffectSilence)

        Effects.register(EffectSetXpChange)

        Effects.register(EffectMultiplyProjectileVelocity)

        Effects.register(EffectStartCombo)
        Effects.register(EffectEndCombo)
        Effects.register(EffectExtendCombo)
        Effects.register(EffectStartOrExtendCombo)

        Effects.register(EffectSetComboRenewalTicks)
        Effects.register(EffectSetComboUpdateTicks)
        Effects.register(EffectSetComboStartTicks)

        Effects.register(EffectSetComboTriggerShouldUpdateEffects)

        Effects.register(EffectSetDamageModifier)

        Effects.register(EffectPutDamageMultiplier)

        Triggers.register(TriggerRiptide)
        Triggers.register(TriggerInteract(this))
        Triggers.register(TriggerInventoryInteract)
        Triggers.register(TriggerInventoryClick)
        Triggers.register(TriggerMineBlockStop)

        TriggerXpChange.registerAllInto(Triggers)

        TriggerTakeDamage.registerAllInto(Triggers)

        TriggerInflictDamage.registerAllInto(Triggers)

        TriggerComboStart.registerAllInto(Triggers)
        TriggerComboEnd.registerAllInto(Triggers)
        TriggerComboTick.registerAllInto(Triggers)

        TriggerOfflineComboEnd.registerAllInto(Triggers)
        TriggerOfflineComboTick.registerAllInto(Triggers)

        Triggers.register(TriggerToggleShield)

        TriggerProjectileHits.registerAllInto(Triggers)
        TriggerHitByProjectile.registerAllInto(Triggers)

        Mutators.register(MutatorAttackDamageAsValue)
        Mutators.register(MutatorAttackDamageAsAltValue)

        Mutators.register(MutatorAttackFinalDamageAsValue)
        Mutators.register(MutatorAttackFinalDamageAsAltValue)

        Mutators.register(MutatorXpChangeAsValue)
        Mutators.register(MutatorXpChangeAsAltValue)

        Conditions.register(ConditionHasCombo)

        Filters.register(FilterInventoryAction)
        Filters.register(FilterInventoryDragType)
        Filters.register(FilterInventoryInteractType)
        Filters.register(FilterPlayerAction)
        Filters.register(FilterTargetReason)

        Filters.register(FilterPlayerIsPresent)
        Filters.register(FilterPlayerIsSilent)
        Filters.register(FilterPlayerIsInvulnerable)
        Filters.register(FilterPlayerHasGravity)

        Filters.register(FilterIsSprinting)
        Filters.register(FilterIsSneaking)
        Filters.register(FilterIsBlocking)

        Filters.register(FilterVictimIsPresent)
        Filters.register(FilterMatchEntitiesIfPresent)
        Filters.register(FilterIgnoreEntitiesIfPresent)

        Filters.register(FilterProjectileIsPresent)
        Filters.register(FilterMatchProjectilesIfPresent)
        Filters.register(FilterIgnoreProjectilesIfPresent)

        Filters.register(FilterDamagerIsPresent)

        FilterTradeSelectIndex.registerAllInto(Filters)

        FilterPlayerNoDamageTicks.registerAllInto(Filters)

        FilterVictimNoDamageTicks.registerAllInto(Filters)

        FilterXpChange.registerAllInto(Filters)

        FilterXpOrbCount.registerAllInto(Filters)

        FilterXpOrbExperience.registerAllInto(Filters)

        Filters.register(FilterMatchXpOrbSpawnReasonIfPresent)
        Filters.register(FilterIgnoreXpOrbSpawnReasonIfPresent)

        Filters.register(FilterHasAnyCombo)

        Filters.register(FilterComboIsPresent)
        Filters.register(FilterMatchComboIfPresent)
        Filters.register(FilterIgnoreComboIfPresent)
        Filters.register(FilterHasCombos)

        FilterComboCount.registerAllInto(Filters)
        FilterComboScore.registerAllInto(Filters)
        FilterComboRemainingTicks.registerAllInto(Filters)

        if (Prerequisite.HAS_1_20_5.isMet) {
            Filters.register(FilterDamageType)
            Filters.register(FilterDamageSourceIsIndirect)
        }

        if (Prerequisite.HAS_PAPER.isMet) {
            PaperIntegration.load(this)
        }

        if (ClassUtils.exists("org.bukkit.Input")) {
            Effects.register(EffectSetPlayerInputShouldUpdateEffects)

            Triggers.register(TriggerPlayerInput)

            FilterPlayerInput.registerAllInto(Filters)

            Conditions.register(ConditionPlayerCurrentInput)
        }

        ComboPlaceholder.createHas(this).register()
        ComboPlaceholder.createCount(this).register()
        ComboPlaceholder.createScore(this).register()
        ComboPlaceholder.createRemainingTicks(this).register()
    }

    override fun loadListeners(): List<Listener> {
        val listeners = mutableListOf<Listener>()
        if (Prerequisite.HAS_PAPER.isMet) {
            listeners.add(ServerTickListener)
        }
        if (ClassUtils.exists("org.bukkit.Input")) {
            listeners.add(PlayerInputListener)
        }
        listeners.add(DamageListener)
        listeners.add(PlayerJoinListener)
        listeners.add(PlayerQuitListener)
        return listeners.toList()
    }
}
