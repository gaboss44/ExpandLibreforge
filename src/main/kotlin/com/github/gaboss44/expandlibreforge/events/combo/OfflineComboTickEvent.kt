package com.github.gaboss44.expandlibreforge.events.combo

import com.github.gaboss44.expandlibreforge.features.combo.Combo
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class OfflineComboTickEvent(
    override val parent: Event? = null,
    combo: Combo,
    override var updateTicks: Int
) : OfflineComboEvent(combo), ComboTickEvent {

    private var cancelled: Boolean = false

    override fun isCancelled() = cancelled

    override fun setCancelled(p0: Boolean) {
        cancelled = p0
    }

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}
