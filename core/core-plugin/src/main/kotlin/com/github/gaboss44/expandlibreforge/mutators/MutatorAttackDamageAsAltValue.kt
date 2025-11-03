package com.github.gaboss44.expandlibreforge.mutators

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.mutators.Mutator
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.entity.EntityDamageEvent

object MutatorAttackDamageAsAltValue : Mutator<NoCompileData>("attack_damage_as_alt_value") {
    override fun mutate(data: TriggerData, config: Config, compileData: NoCompileData): TriggerData {
        val event = data.event as? EntityDamageEvent ?: return data
        return data.copy(
            altValue = event.damage
        )
    }
}