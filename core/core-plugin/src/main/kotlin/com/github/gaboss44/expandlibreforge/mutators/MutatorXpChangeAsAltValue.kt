package com.github.gaboss44.expandlibreforge.mutators

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.events.NaturalExpGainEvent
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.mutators.Mutator
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.player.PlayerExpChangeEvent

object MutatorXpChangeAsAltValue : Mutator<NoCompileData>("xp_change_as_alt_value") {
    override fun mutate(data: TriggerData, config: Config, compileData: NoCompileData): TriggerData {
        val event = data.event ?: return data
        val amount = when (event) {
            is PlayerExpChangeEvent -> event.amount
            is NaturalExpGainEvent -> event.expChangeEvent.amount
            else -> return data
        }
        return data.copy(
            altValue = amount.toDouble()
        )
    }
}