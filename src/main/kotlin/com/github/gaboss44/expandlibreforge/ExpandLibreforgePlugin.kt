package com.github.gaboss44.expandlibreforge

import com.github.gaboss44.expandlibreforge.effects.*
import com.github.gaboss44.expandlibreforge.filters.*
import com.github.gaboss44.expandlibreforge.triggers.*
import com.github.gaboss44.expandlibreforge.mutators.*
import com.github.gaboss44.expandlibreforge.integrations.*
import com.willfp.eco.core.Prerequisite
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.mutators.Mutators
import com.willfp.libreforge.triggers.Triggers

class ExpandLibreforgePlugin : LibreforgePlugin() {

    init {
        ExpandLibreforgeProps.register(this)
    }

    override fun handleEnable() {

        Effects.register(EffectPlaySoundKey)

        Effects.register(EffectSetGravity)
        Effects.register(EffectSetSilent)

        Effects.register(EffectAntigravity)
        Effects.register(EffectInvulnerability)
        Effects.register(EffectSilence)

        Effects.register(EffectSetXpChange)

        Triggers.register(TriggerRiptide)
        Triggers.register(TriggerInteract)
        Triggers.register(TriggerInventoryInteract)
        Triggers.register(TriggerInventoryClick)
        Triggers.register(TriggerMineBlockStop)

        Triggers.register(TriggerXpChange.LowestPriority)
        Triggers.register(TriggerXpChange.LowPriority)
        Triggers.register(TriggerXpChange.NormalPriority)
        Triggers.register(TriggerXpChange.HighPriority)
        Triggers.register(TriggerXpChange.HighestPriority)

        Triggers.register(TriggerTakeDamage.HighestPriority)
        Triggers.register(TriggerTakeDamage.HighPriority)
        Triggers.register(TriggerTakeDamage.NormalPriority)
        Triggers.register(TriggerTakeDamage.LowPriority)
        Triggers.register(TriggerTakeDamage.LowestPriority)

        Triggers.register(TriggerInflictDamage.HighestPriority)
        Triggers.register(TriggerInflictDamage.HighPriority)
        Triggers.register(TriggerInflictDamage.NormalPriority)
        Triggers.register(TriggerInflictDamage.LowPriority)
        Triggers.register(TriggerInflictDamage.LowestPriority)

        Mutators.register(MutatorAttackDamageAsValue)
        Mutators.register(MutatorAttackDamageAsAltValue)

        Mutators.register(MutatorAttackFinalDamageAsValue)
        Mutators.register(MutatorAttackFinalDamageAsAltValue)

        Mutators.register(MutatorXpChangeAsValue)
        Mutators.register(MutatorXpChangeAsAltValue)

        Filters.register(FilterInventoryAction)
        Filters.register(FilterInventoryDragType)
        Filters.register(FilterInventoryInteractType)
        Filters.register(FilterPlayerAction)
        Filters.register(FilterTargetReason)

        Filters.register(FilterPlayerIsPresent)
        Filters.register(FilterPlayerIsSilent)
        Filters.register(FilterPlayerIsInvulnerable)
        Filters.register(FilterPlayerHasGravity)

        Filters.register(FilterVictimIsPresent)

        Filters.register(FilterProjectileIsPresent)
        Filters.register(FilterMatchProjectilesIfPresent)
        Filters.register(FilterIgnoreProjectilesIfPresent)

        Filters.register(FilterDamagerIsPresent)

        Filters.register(FilterTradeSelectIndex.AtLeast)
        Filters.register(FilterTradeSelectIndex.AtMost)
        Filters.register(FilterTradeSelectIndex.Equals)
        Filters.register(FilterTradeSelectIndex.GreaterThan)
        Filters.register(FilterTradeSelectIndex.LowerThan)

        Filters.register(FilterPlayerNoDamageTicks.AtLeast)
        Filters.register(FilterPlayerNoDamageTicks.AtMost)
        Filters.register(FilterPlayerNoDamageTicks.Equals)
        Filters.register(FilterPlayerNoDamageTicks.GreaterThan)
        Filters.register(FilterPlayerNoDamageTicks.LowerThan)

        Filters.register(FilterXpChange.AtLeast)
        Filters.register(FilterXpChange.AtMost)
        Filters.register(FilterXpChange.Equals)
        Filters.register(FilterXpChange.GreaterThan)
        Filters.register(FilterXpChange.LowerThan)

        Filters.register(FilterXpOrbCount.AtLeast)
        Filters.register(FilterXpOrbCount.AtMost)
        Filters.register(FilterXpOrbCount.Equals)
        Filters.register(FilterXpOrbCount.GreaterThan)
        Filters.register(FilterXpOrbCount.LowerThan)

        Filters.register(FilterXpOrbExperience.AtLeast)
        Filters.register(FilterXpOrbExperience.AtMost)
        Filters.register(FilterXpOrbExperience.Equals)
        Filters.register(FilterXpOrbExperience.GreaterThan)
        Filters.register(FilterXpOrbExperience.LowerThan)

        Filters.register(FilterXpOrbSpawnReason)
        Filters.register(FilterXpOrbNotSpawnReason)

        if (Prerequisite.HAS_1_20_5.isMet) {

            Filters.register(FilterDamageType)
            Filters.register(FilterDamageSourceIsIndirect)

        }

        if (Prerequisite.HAS_PAPER.isMet) {
            PaperIntegration.load(this)
        }
    }
}
