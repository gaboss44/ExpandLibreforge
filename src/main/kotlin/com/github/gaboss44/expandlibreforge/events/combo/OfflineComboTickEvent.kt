package com.github.gaboss44.expandlibreforge.events.combo

import com.github.gaboss44.expandlibreforge.events.Parental
import com.github.gaboss44.expandlibreforge.features.combo.Combo
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class OfflineComboTickEvent(
    override val parent: Event? = null,
    combo: Combo,
    updateTicks: Int
) : OfflineComboEvent(combo), Cancellable, Parental {

    private var ticks: Int = updateTicks

    var updateTicks: Int
        get() = ticks
        set(value) { ticks = value }

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
