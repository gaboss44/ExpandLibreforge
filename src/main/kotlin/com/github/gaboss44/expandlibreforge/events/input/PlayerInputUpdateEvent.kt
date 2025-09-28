package com.github.gaboss44.expandlibreforge.events.input

import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInputEvent

class PlayerInputUpdateEvent(
    val parent: PlayerInputEvent,
    private var updateEffects: Boolean
) : PlayerEvent(parent.player) {

    fun shouldUpdateEffects() = updateEffects

    fun setShouldUpdateEffects(value: Boolean) { updateEffects = value }

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}