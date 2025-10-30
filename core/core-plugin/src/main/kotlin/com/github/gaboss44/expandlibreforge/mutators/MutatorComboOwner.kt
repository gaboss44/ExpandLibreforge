package com.github.gaboss44.expandlibreforge.mutators

import com.github.gaboss44.expandlibreforge.events.combo.PlayerComboEvent
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.mutators.Mutator
import com.willfp.libreforge.mutators.Mutators
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.Player

sealed class MutatorComboOwner(id: String) : Mutator<NoCompileData>(id) {
    final override fun mutate(data: TriggerData, config: Config, compileData: NoCompileData): TriggerData {
        val event = data.event as? PlayerComboEvent ?: return data
        return mutate(data, config, event.player)
    }

    abstract fun mutate(data: TriggerData, config: Config, owner: Player): TriggerData

    companion object {
        fun registerAllInto(category: Mutators) {
            category.register(AsPlayer)
            category.register(AsVictim)
        }
    }

    object AsPlayer : MutatorComboOwner("combo_owner_as_player") {
        override fun mutate(data: TriggerData, config: Config, owner: Player): TriggerData {
            return data.copy(player = owner)
        }
    }

    object AsVictim : MutatorComboOwner("combo_owner_as_victim") {
        override fun mutate(data: TriggerData, config: Config, owner: Player): TriggerData {
            return data.copy(victim = owner)
        }
    }
}