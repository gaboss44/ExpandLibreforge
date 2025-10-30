package com.github.gaboss44.expandlibreforge.proxy.v1_21_4

import com.github.gaboss44.expandlibreforge.proxies.AttackResolutionHandlerProxy
import com.willfp.eco.core.Prerequisite
import io.papermc.paper.event.entity.EntityKnockbackEvent
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.boss.EnderDragonPart
import net.minecraft.world.entity.decoration.ArmorStand
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.craftbukkit.entity.CraftEntity
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.craftbukkit.util.CraftVector
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExhaustionEvent
import org.bukkit.event.player.PlayerVelocityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

@Suppress("UnstableApiUsage")
class AttackResolutionHandler : AttackResolutionHandlerProxy {

    override fun hurtOrSimulate(
        target: Entity,
        source: DamageSource,
        damage: Float
    ): Boolean {
        val nmsTarget = (target as CraftEntity).handle!!
        val nmsSource = (source as CraftDamageSource).handle!!
        return nmsTarget.hurtOrSimulate(nmsSource, damage)
    }

    override fun resolveAttack(
        attacker: Player,
        target: Entity,
        weapon: ItemStack,
        damageSource: DamageSource,
        baseDamage: Float,
        enchantedDamage: Float,
        attackStrengthScale: Float,
        didStrongAttack: Boolean,
        didCriticalAttack: Boolean,
        didSprintAttack: Boolean,
        didSweepAttack: Boolean,
        strongAttackSoundEffects: () -> Unit,
        weakAttackSoundEffects: () -> Unit,
        criticalAttackSoundEffects: () -> Unit,
        sweepAttackSoundEffects: () -> Unit,
        enchantedKnockbackEffects: (Entity, DamageSource, Float) -> Float,
        enchantedSweepDamageEffects: (Entity, DamageSource, Float) -> Float,
        onPostWeaponAttack: (Entity, DamageSource) -> Unit,
        onPreSweepAttack: (Entity, DamageSource) -> Boolean,
        onPostSweepAttack: (Entity, DamageSource) -> Unit
    ) {
        val nmsAttacker = (attacker as CraftPlayer).handle!!
        val nmsLevel = nmsAttacker.level()
        val nmsTarget = (target as CraftEntity).handle!!
        var mirrorWeapon = false
        val nmsWeapon = (weapon as? CraftItemStack)?.handle?.let {
            mirrorWeapon = true
            it
        } ?: CraftItemStack.asNMSCopy(weapon)!!
        val nmsSource = (damageSource as CraftDamageSource).handle!!
        val f4 = nmsAttacker.getAttributeValue(Attributes.ATTACK_KNOCKBACK).toFloat().let {
            if (nmsLevel is ServerLevel) {
                enchantedKnockbackEffects(attacker, damageSource, it)
            } else {
                it
            }
        } + if (didSprintAttack) 1.0f else 0.0f
        if (f4 > 0.0f) {
            if (nmsTarget is net.minecraft.world.entity.LivingEntity) {
                if (Prerequisite.HAS_PAPER.isMet) {
                    nmsTarget.knockback(
                        f4.toDouble() * 0.5F, Mth.sin((nmsAttacker.yRot * (Math.PI / 180.0)).toFloat()).toDouble(), (-Mth.cos(nmsAttacker.yRot * (Math.PI / 180.0).toFloat())).toDouble()
                        , nmsAttacker, EntityKnockbackEvent.Cause.ENTITY_ATTACK // Paper - knockback events
                    )
                } else {
                    nmsTarget.knockback(
                        f4.toDouble() * 0.5F, Mth.sin((nmsAttacker.yRot * (Math.PI / 180.0)).toFloat()).toDouble(), (-Mth.cos(nmsAttacker.yRot * (Math.PI / 180.0).toFloat())).toDouble()
                    )
                }
            } else {
                if (Prerequisite.HAS_PAPER.isMet) {
                    nmsTarget.push(
                        (-Mth.sin(nmsAttacker.yRot * (Math.PI / 180.0).toFloat()) * f4 * 0.5f).toDouble(),
                        0.1,
                        (Mth.cos(nmsAttacker.yRot * (Math.PI / 180.0).toFloat()) * f4 * 0.5f).toDouble(),
                        nmsAttacker
                    )
                } else {
                    nmsTarget.push(
                        (-Mth.sin(nmsAttacker.yRot * (Math.PI / 180.0).toFloat()) * f4 * 0.5f).toDouble(),
                        0.1,
                        (Mth.cos(nmsAttacker.yRot * (Math.PI / 180.0).toFloat()) * f4 * 0.5f).toDouble()
                    )
                }
            }

            nmsAttacker.deltaMovement = nmsAttacker.deltaMovement.multiply(0.6, 1.0, 0.6)

            if (Prerequisite.HAS_PAPER.isMet) {
                try {
                    if (!nmsLevel.paperConfig().misc.disableSprintInterruptionOnAttack) {
                        nmsAttacker.isSprinting = false
                    }
                } catch (_: Throwable) {
                    nmsAttacker.isSprinting = false
                }
            } else {
                nmsAttacker.isSprinting = false
            }
        }

        if (didSweepAttack) {
            val f5: Float = 1.0f + nmsAttacker.getAttributeValue(Attributes.SWEEPING_DAMAGE_RATIO).toFloat() * baseDamage

            for (livingEntity2 in nmsLevel
                .getEntitiesOfClass(
                    net.minecraft.world.entity.LivingEntity::class.java,
                    nmsTarget.boundingBox.inflate(1.0, 0.25, 1.0)
                )) {
                if (livingEntity2 !== nmsAttacker && livingEntity2 !== nmsTarget && !nmsAttacker.isAlliedTo(livingEntity2) && !(livingEntity2 is ArmorStand && livingEntity2.isMarker) && nmsAttacker.distanceToSqr(
                        livingEntity2
                    ) < 9.0
                ) {
                    val f6: Float = enchantedSweepDamageEffects(livingEntity2.bukkitLivingEntity, damageSource, f5) * attackStrengthScale
                    livingEntity2.lastDamageCancelled = false
                    val nmsSweepSource = nmsSource.knownCause(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
                    val sweepSource = CraftDamageSource(nmsSweepSource)
                    if (nmsLevel is ServerLevel &&
                        onPreSweepAttack(livingEntity2.bukkitLivingEntity, sweepSource) &&
                        livingEntity2.hurtServer(
                            nmsLevel,
                            nmsSweepSource,
                            f6
                        ) &&
                        !livingEntity2.lastDamageCancelled
                    ) {
                        if (Prerequisite.HAS_PAPER.isMet) livingEntity2.knockback(
                            0.4,
                            Mth.sin(nmsAttacker.yRot * (Math.PI / 180.0).toFloat()).toDouble(),
                            -Mth.cos(nmsAttacker.yRot * (Math.PI / 180.0).toFloat()).toDouble(),
                            nmsAttacker,
                            EntityKnockbackEvent.Cause.SWEEP_ATTACK // Paper - knockback events
                        ) else {
                            livingEntity2.knockback(
                                0.4,
                                Mth.sin(nmsAttacker.yRot * (Math.PI / 180.0).toFloat()).toDouble(),
                                -Mth.cos(nmsAttacker.yRot * (Math.PI / 180.0).toFloat()).toDouble()
                            )
                        }
                        onPostSweepAttack(livingEntity2.bukkitLivingEntity, CraftDamageSource(nmsSource.knownCause(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)))
                    }
                }
            }

            sweepAttackSoundEffects()
            nmsAttacker.sweepAttack()
        }

        val targetMovement = nmsTarget.deltaMovement
        if (nmsTarget is ServerPlayer && nmsTarget.hurtMarked) {
            // CraftBukkit start - Add Velocity Event
            var cancelled = false
            val player = nmsTarget.bukkitEntity as Player
            val velocity: Vector = CraftVector.toBukkit(targetMovement)

            val event = PlayerVelocityEvent(player, velocity.clone())
            nmsLevel.craftServer.pluginManager.callEvent(event)

            if (event.isCancelled) {
                cancelled = true
            } else if (velocity != event.velocity) {
                player.velocity = event.velocity
            }

            if (!cancelled) {
                nmsTarget.connection.send(ClientboundSetEntityMotionPacket(nmsTarget))
                nmsTarget.hurtMarked = false
                nmsTarget.deltaMovement = targetMovement
            }
            // CraftBukkit end
        }

        if (didCriticalAttack) {
            criticalAttackSoundEffects()
            nmsAttacker.crit(nmsTarget)
        }

        if (!didCriticalAttack && !didSweepAttack) {
            if (didStrongAttack) {
                strongAttackSoundEffects()
            } else {
                weakAttackSoundEffects()
            }
        }

        if (enchantedDamage > 0.0f) {
            nmsAttacker.magicCrit(nmsTarget)
        }

        nmsAttacker.setLastHurtMob(nmsTarget)
        var entity: net.minecraft.world.entity.Entity = nmsTarget
        if (nmsTarget is EnderDragonPart) {
            entity = nmsTarget.parentMob
        }

        var flag5 = false
        if (nmsLevel is ServerLevel) {
            if (entity is net.minecraft.world.entity.LivingEntity) {
                flag5 = nmsWeapon.hurtEnemy(entity, nmsAttacker)
            }

            onPostWeaponAttack(entity.bukkitEntity, damageSource)
        }

        if (!nmsLevel.isClientSide() && !nmsWeapon.isEmpty && entity is net.minecraft.world.entity.LivingEntity) {
            if (flag5) {
                nmsWeapon.postHurtEnemy(entity, nmsAttacker)
            }

            if (nmsWeapon.isEmpty && mirrorWeapon) {
                if (nmsWeapon == nmsAttacker.mainHandItem) {
                    nmsAttacker.setItemInHand(InteractionHand.MAIN_HAND, net.minecraft.world.item.ItemStack.EMPTY)
                } else {
                    nmsAttacker.setItemInHand(InteractionHand.OFF_HAND, net.minecraft.world.item.ItemStack.EMPTY)
                }
            }
        }

        val f3 = if (nmsTarget is net.minecraft.world.entity.LivingEntity) {
            nmsTarget.health
        } else {
            0.0f
        }
        if (nmsTarget is net.minecraft.world.entity.LivingEntity) {
            val f7: Float = f3 - nmsTarget.health
            nmsAttacker.awardStat(Stats.DAMAGE_DEALT, Math.round(f7 * 10.0f))
            if (nmsLevel is ServerLevel && f7 > 2.0f) {
                val i = (f7 * 0.5).toInt()
                nmsLevel.sendParticles<SimpleParticleType?>(
                        ParticleTypes.DAMAGE_INDICATOR,
                        nmsTarget.x,
                    nmsTarget.getY(0.5),
                    nmsTarget.z,
                        i,
                        0.1,
                        0.0,
                        0.1,
                        0.2
                    )
            }
        }

        nmsAttacker.causeFoodExhaustion(
            nmsLevel.spigotConfig.combatExhaustion,
            EntityExhaustionEvent.ExhaustionReason.ATTACK
        ) // CraftBukkit - EntityExhaustionEvent // Spigot - Change to use configurable value
    }
}
