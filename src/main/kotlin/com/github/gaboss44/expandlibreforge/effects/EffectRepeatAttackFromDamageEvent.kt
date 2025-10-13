package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.Damageable
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EffectRepeatAttackFromDamageEvent : Effect<NoCompileData>("repeat_attack_from_damage_event") {

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDamageByEntityEvent ?: return false
        val source = event.damageSource
        val entity = event.entity as? Damageable ?: return false

        val damageStr = event.damage.toString()
        val finalDamageStr = event.finalDamage.toString()
        val damage = NumberUtils.evaluateExpression((config.getStringOrNull("damage") ?: return false)
            .replace("%damage%", damageStr)
            .replace("%d%", damageStr)
            .replace("%final_damage%", finalDamageStr)
            .replace("%fd%", finalDamageStr),
            config.toPlaceholderContext(data))

        entity.damage(damage, source)
        return true
    }
}