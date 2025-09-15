package com.github.gaboss44.expandlibreforge.util

import com.willfp.eco.core.Prerequisite
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Suppress("UnstableApiUsage")
fun EntityDamageByEntityEvent.tryDamagerAsPlayer(): Player? {
    return when (val damager = this.damager) {
        is Player -> damager
        is Projectile -> damager.shooter as? Player
        else -> if (Prerequisite.HAS_1_20_5.isMet) {
            damageSource.causingEntity as? Player ?:
            damageSource.directEntity as? Player
        } else null
    }
}

@Suppress("UnstableApiUsage")
fun EntityDamageByEntityEvent.tryDamagerAsLivingEntity(): LivingEntity? {
    return when (val damager = this.damager) {
        is LivingEntity -> damager
        is Projectile -> damager.shooter as? LivingEntity
        else -> if (Prerequisite.HAS_1_20_5.isMet) {
            damageSource.causingEntity as? LivingEntity ?:
            damageSource.directEntity as? LivingEntity
        } else null
    }
}

@Suppress("UnstableApiUsage")
fun EntityDamageByEntityEvent.tryDamagerAsProjectile(): Projectile? {
    return when (val damager = this.damager) {
        is Projectile -> damager
        else -> if (Prerequisite.HAS_1_20_5.isMet) {
            damageSource.directEntity as? Projectile
        } else null
    }
}
