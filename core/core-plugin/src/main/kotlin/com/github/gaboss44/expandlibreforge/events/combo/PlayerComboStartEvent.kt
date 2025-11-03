package com.github.gaboss44.expandlibreforge.events.combo

import com.github.gaboss44.expandlibreforge.features.combo.Combo
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerComboStartEvent(
    override val parent: Event? = null,
    player: Player,
    combo: Combo,
    shouldUpdateEffects: Boolean = false,
    override var startTicks: Int = 0
) : PlayerComboEvent(player, combo, shouldUpdateEffects), ComboStartEvent {

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