package com.github.gaboss44.expandlibreforge.events.combo

import com.github.gaboss44.expandlibreforge.features.combo.Combo
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerComboTickEvent(
    override val parent: Event? = null,
    player: Player,
    combo: Combo,
    shouldUpdateEffects: Boolean = false,
    override var updateTicks: Int
) : PlayerComboEvent(player, combo, shouldUpdateEffects), ComboTickEvent {

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

    enum class Reason(val lowerValue: String) {
        CYCLE("cycle"),
        END("end");

        companion object {
            private val byLowerValue = entries.associateBy { it.lowerValue }

            operator fun get(lowerValue: String?) = byLowerValue[lowerValue?.lowercase()]
        }
    }
}