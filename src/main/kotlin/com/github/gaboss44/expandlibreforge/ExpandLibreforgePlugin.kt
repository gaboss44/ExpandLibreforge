package com.github.gaboss44.expandlibreforge

import com.github.gaboss44.expandlibreforge.effects.EffectDoRiptideSpin
import com.github.gaboss44.expandlibreforge.filters.FilterInventoryAction
import com.github.gaboss44.expandlibreforge.filters.FilterInventoryDragType
import com.github.gaboss44.expandlibreforge.filters.FilterInventoryInteractType
import com.github.gaboss44.expandlibreforge.filters.FilterIsCriticalHit
import com.github.gaboss44.expandlibreforge.filters.FilterPlayerAction
import com.github.gaboss44.expandlibreforge.filters.FilterTradeSelectIndexAtLeast
import com.github.gaboss44.expandlibreforge.filters.FilterTradeSelectIndexAtMost
import com.github.gaboss44.expandlibreforge.filters.FilterTradeSelectIndexEquals
import com.github.gaboss44.expandlibreforge.filters.FilterTradeSelectIndexGreaterThan
import com.github.gaboss44.expandlibreforge.filters.FilterTradeSelectIndexLowerThan
import com.github.gaboss44.expandlibreforge.triggers.TriggerInventoryInteract
import com.github.gaboss44.expandlibreforge.triggers.TriggerPlayerInteract
import com.github.gaboss44.expandlibreforge.triggers.TriggerPlayerRiptide
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.triggers.Triggers

class ExpandLibreforgePlugin : LibreforgePlugin() {

    init {
        ExpandLibreforgeProps.register(this)
    }

    override fun handleEnable() {

        Triggers.register(TriggerPlayerRiptide)
        Triggers.register(TriggerPlayerInteract)
        Triggers.register(TriggerInventoryInteract)

        Effects.register(EffectDoRiptideSpin)

        Filters.register(FilterInventoryAction)
        Filters.register(FilterInventoryDragType)
        Filters.register(FilterInventoryInteractType)
        Filters.register(FilterIsCriticalHit)
        Filters.register(FilterPlayerAction)
        Filters.register(FilterTradeSelectIndexAtLeast)
        Filters.register(FilterTradeSelectIndexAtMost)
        Filters.register(FilterTradeSelectIndexEquals)
        Filters.register(FilterTradeSelectIndexGreaterThan)
        Filters.register(FilterTradeSelectIndexLowerThan)
    }
}
