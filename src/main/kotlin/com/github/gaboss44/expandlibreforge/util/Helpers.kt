package com.github.gaboss44.expandlibreforge.util

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.config.interfaces.Config
import io.papermc.paper.event.player.PlayerShieldDisableEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

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

fun EntityDamageByEntityEvent.tryDamagerAsProjectile(): Projectile? {
    return when (val damager = this.damager) {
        is Projectile -> damager
        else -> if (Prerequisite.HAS_1_20_5.isMet) {
            damageSource.directEntity as? Projectile
        } else null
    }
}

fun PlayerShieldDisableEvent.tryDamagerAsPlayer(): Player? {
    return when (val damager = this.damager) {
        is Player -> damager
        is Projectile -> damager.shooter as? Player
        else -> null
    }
}

fun PlayerShieldDisableEvent.tryDamagerAsLivingEntity(): LivingEntity? {
    return when (val damager = this.damager) {
        is LivingEntity -> damager
        is Projectile -> damager.shooter as? LivingEntity
        else -> null
    }
}

fun PlayerShieldDisableEvent.tryDamagerAsProjectile(): Projectile? {
    return when (val damager = this.damager) {
        is Projectile -> damager
        else -> null
    }
}

fun EntityKnockbackByEntityEvent.tryDamagerAsPlayer(): Player? {
    return when (val damager = this.hitBy) {
        is Player -> damager
        is Projectile -> damager.shooter as? Player
        else -> null
    }
}

fun EntityKnockbackByEntityEvent.tryDamagerAsLivingEntity(): LivingEntity? {
    return when (val damager = this.hitBy) {
        is LivingEntity -> damager
        is Projectile -> damager.shooter as? LivingEntity
        else -> null
    }
}

fun EntityKnockbackByEntityEvent.tryDamagerAsProjectile(): Projectile? {
    return when (val damager = this.hitBy) {
        is Projectile -> damager
        else -> null
    }
}

@Suppress("DEPRECATION")
fun Config.getDamageModifierEnum(key: String): EntityDamageEvent.DamageModifier? {
    val string = this.getStringOrNull(key) ?: return null
    return try {
        EntityDamageEvent.DamageModifier.valueOf(string.uppercase())
    } catch (_: IllegalArgumentException) {
        null
    }
}
