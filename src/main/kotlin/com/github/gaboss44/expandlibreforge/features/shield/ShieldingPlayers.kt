package com.github.gaboss44.expandlibreforge.features.shield

import com.github.gaboss44.expandlibreforge.events.shield.PlayerToggleShieldEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import java.util.UUID

object ShieldingPlayers {

    private val delegate = mutableSetOf<UUID>()

    fun add(playerId: UUID) = delegate.add(playerId)

    fun remove(playerId: UUID) = delegate.remove(playerId)

    fun contains(playerId: UUID) = delegate.contains(playerId)

    private fun checkShield(event: Event?, player: Player): Boolean {
        val nowBlocking = player.isBlocking
        val wasBlocking = contains(player.uniqueId)
        val toggled = nowBlocking != wasBlocking
        if (toggled) {
            val event = PlayerToggleShieldEvent(event, player, nowBlocking)
            event.callEvent()
            if (event.isNowBlocking()) {
                delegate.add(player.uniqueId)
            } else {
                delegate.remove(player.uniqueId)
            }
            return true
        }
        return false
    }

    fun checkAllShields(event: Event?): List<UUID> {
        val toggledShields = mutableListOf<UUID>()
        for (player in Bukkit.getOnlinePlayers()) {
            if (checkShield(event, player)) toggledShields.add(player.uniqueId)
        }
        return toggledShields.toList()
    }
}
