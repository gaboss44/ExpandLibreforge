package com.github.gaboss44.expandlibreforge.events.combo

import com.github.gaboss44.expandlibreforge.features.combo.Combo
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

abstract class PlayerComboEvent(
    player: Player,
    override val combo: Combo,
    var shouldUpdateEffects: Boolean
) : PlayerEvent(player), ComboEvent {

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}