package com.github.gaboss44.expandlibreforge.proxy.v1_21_5

import com.github.gaboss44.expandlibreforge.proxies.WorldAccessorProxy
import com.github.gaboss44.expandlibreforge.proxy.common.toNMS
import com.willfp.eco.core.Prerequisite
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.level.block.LevelEvent
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

class WorldAccessor : WorldAccessorProxy {

    override fun arePlayerCritsDisabled(world: World): Boolean {
        if (!Prerequisite.HAS_PAPER.isMet) return false
        return world.toNMS().paperConfig().entities.behavior.disablePlayerCrits
    }

    override fun isSprintInterruptionOnAttackDisabled(world: World): Boolean {
        if (!Prerequisite.HAS_PAPER.isMet) return false
        return world.toNMS().paperConfig().misc.disableSprintInterruptionOnAttack
    }

    override fun sendSmashAttackParticles(
        world: World,
        vector: Vector
    ) {
        world.toNMS().levelEvent(
            LevelEvent.PARTICLES_SMASH_ATTACK,
            BlockPos(
                vector.blockX,
                vector.blockY,
                vector.blockZ
            ),
            750
        )
    }

    override fun sendSimpleDamageIndicatorParticles(
        world: World,
        target: Entity,
        amount: Int
    ) {
        world.toNMS().sendParticles(
            ParticleTypes.DAMAGE_INDICATOR,
            target.x,
            target.toNMS().getY(0.5),
            target.z,
            amount,
            0.1,
            0.0,
            0.1,
            0.2
        )
    }

    override fun getCombatExhaustion(world: World): Float {
        return world.toNMS().spigotConfig.combatExhaustion
    }
}