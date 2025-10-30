package com.github.gaboss44.expandlibreforge.events.shield

import com.github.gaboss44.expandlibreforge.events.Parental
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class PlayerToggleShieldEvent(
    override val parent: Event?,
    player: Player,
    private val nowBlocking: Boolean
) : PlayerEvent(player), Parental {

    fun isNowBlocking() = nowBlocking

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}