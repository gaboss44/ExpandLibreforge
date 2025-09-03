package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.utils.IsCriticalHitMethod
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.event.entity.EntityDamageByEntityEvent

object FilterIsCriticalHit : Filter<Boolean, Boolean>("is_critical_hit") {
    override fun getValue(
        config: Config,
        data: TriggerData?,
        key: String
    ) = config.getBoolOrNull(key) ?: true

    override fun makeCompileData(config: Config, context: ViolationContext, values: Boolean): Boolean {
        return values
    }

    override fun isMet(
        data: TriggerData,
        value: Boolean,
        compileData: Boolean
    ) : Boolean {
        val event = data.event as? EntityDamageByEntityEvent ?: return false
        return IsCriticalHitMethod.invoke(event)
    }
}