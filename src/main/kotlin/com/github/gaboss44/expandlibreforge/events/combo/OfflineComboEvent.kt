package com.github.gaboss44.expandlibreforge.events.combo

import com.github.gaboss44.expandlibreforge.features.combo.Combo
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

abstract class OfflineComboEvent(
    override val combo: Combo
) : Event(), ComboEvent {

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}