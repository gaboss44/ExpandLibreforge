package com.github.gaboss44.expandlibreforge

import com.github.gaboss44.expandlibreforge.effects.EffectPlaySoundKey
import com.github.gaboss44.expandlibreforge.effects.EffectSetGravity
import com.github.gaboss44.expandlibreforge.effects.EffectSetSilent
import com.github.gaboss44.expandlibreforge.filters.FilterInventoryAction
import com.github.gaboss44.expandlibreforge.filters.FilterInventoryDragType
import com.github.gaboss44.expandlibreforge.filters.FilterInventoryInteractType
import com.github.gaboss44.expandlibreforge.filters.FilterPlayerAction
import com.github.gaboss44.expandlibreforge.filters.FilterTargetReason
import com.github.gaboss44.expandlibreforge.filters.FilterTradeSelectIndexAtLeast
import com.github.gaboss44.expandlibreforge.filters.FilterTradeSelectIndexAtMost
import com.github.gaboss44.expandlibreforge.filters.FilterTradeSelectIndexEquals
import com.github.gaboss44.expandlibreforge.filters.FilterTradeSelectIndexGreaterThan
import com.github.gaboss44.expandlibreforge.filters.FilterTradeSelectIndexLowerThan
import com.github.gaboss44.expandlibreforge.integrations.PaperIntegration
import com.github.gaboss44.expandlibreforge.triggers.TriggerInventoryClick
import com.github.gaboss44.expandlibreforge.triggers.TriggerInventoryInteract
import com.github.gaboss44.expandlibreforge.triggers.TriggerMineBlockStop
import com.github.gaboss44.expandlibreforge.triggers.TriggerPlayerInteract
import com.github.gaboss44.expandlibreforge.triggers.TriggerPlayerRiptide
import com.willfp.eco.core.Prerequisite
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.triggers.Triggers

class ExpandLibreforgePlugin : LibreforgePlugin() {

    init {
        ExpandLibreforgeProps.register(this)
    }

    override fun handleEnable() {

        Effects.register(EffectPlaySoundKey)
        Effects.register(EffectSetGravity)
        Effects.register(EffectSetSilent)

        Triggers.register(TriggerPlayerRiptide)
        Triggers.register(TriggerPlayerInteract)
        Triggers.register(TriggerInventoryInteract)
        Triggers.register(TriggerInventoryClick)
        Triggers.register(TriggerMineBlockStop)

        Filters.register(FilterInventoryAction)
        Filters.register(FilterInventoryDragType)
        Filters.register(FilterInventoryInteractType)
        Filters.register(FilterPlayerAction)
        Filters.register(FilterTargetReason)
        Filters.register(FilterTradeSelectIndexAtLeast)
        Filters.register(FilterTradeSelectIndexAtMost)
        Filters.register(FilterTradeSelectIndexEquals)
        Filters.register(FilterTradeSelectIndexGreaterThan)
        Filters.register(FilterTradeSelectIndexLowerThan)

        if (Prerequisite.HAS_PAPER.isMet) {
            PaperIntegration.load(this)
        }
    }
}
