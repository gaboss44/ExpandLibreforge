package com.github.gaboss44.expandlibreforge.proxy.v1_21_8

import com.github.gaboss44.expandlibreforge.proxies.EntityAccessorProxy
import com.github.gaboss44.expandlibreforge.proxy.common.toNMS
import io.papermc.paper.event.entity.EntityKnockbackEvent
import net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.stats.Stats
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.phys.Vec3
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.World
import org.bukkit.craftbukkit.event.CraftEventFactory
import org.bukkit.craftbukkit.util.CraftVector
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExhaustionEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

@Suppress("UnstableApiUsage")
class EntityAccessor : EntityAccessorProxy {

    override fun getAttackDamage(entity: LivingEntity): Double {
        return entity.toNMS().getAttributeValue(Attributes.ATTACK_DAMAGE)
    }

    override fun getSpeed(entity: LivingEntity): Double {
        return entity.toNMS().let {
            if (it is net.minecraft.world.entity.player.Player) {
                it.getAttributeValue(Attributes.MOVEMENT_SPEED)
            } else {
                it.speed.toDouble()
            }
        }
    }

    override fun getAttackKnockback(entity: LivingEntity): Double {
        return entity.toNMS().getAttributeValue(Attributes.ATTACK_KNOCKBACK)
    }

    override fun getKnockbackResistance(entity: LivingEntity): Double {
        return entity.toNMS().getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)
    }

    override fun getSweepDamageRatio(entity: LivingEntity): Double {
        return entity.toNMS().getAttributeValue(Attributes.SWEEPING_DAMAGE_RATIO)
    }

    override fun hurtOrSimulate(
        target: Entity,
        source: DamageSource,
        damage: Float
    ): Boolean {
        return target.toNMS().hurtOrSimulate(source.toNMS(), damage)
    }

    override fun hurtServer(
        target: Entity,
        world: World,
        source: DamageSource,
        damage: Float
    ): Boolean {
        return target.toNMS().hurtServer(world.toNMS(), source.toNMS(), damage)
    }

    override fun knockback(
        target: LivingEntity,
        strength: Double,
        x: Double,
        z: Double
    ) {
        target.toNMS().knockback(strength, x, z)
    }

    override fun knockback(
        target: LivingEntity,
        strength: Double,
        x: Double,
        z: Double,
        attacker: Entity?,
        paperCause: EntityKnockbackEvent.Cause
    ) {
        target.toNMS().knockback(strength, x, z, attacker?.toNMS(), paperCause)
    }

    override fun knockbackWithoutResistance(
        target: LivingEntity,
        strength: Double,
        x: Double,
        z: Double,
        attacker: Entity?,
        paperCause: EntityKnockbackEvent.Cause
    ) {
        val target = target.toNMS()
        val attacker = attacker?.toNMS()
        var x1 = x
        var z1 = z

        val deltaMovement = target.deltaMovement

        while (x1 * x1 + z1 * z1 < 1.0E-5) {
            x1 = (Math.random() - Math.random()) * 0.01
            z1 = (Math.random() - Math.random()) * 0.01
        }

        val vec3 = Vec3(x1, 0.0, z1).normalize().scale(strength)

        val finalVelocity = Vec3(
            deltaMovement.x / 2.0 - vec3.x,
            if (target.onGround()) minOf(0.4, deltaMovement.y / 2.0 + strength) else deltaMovement.y,
            deltaMovement.z / 2.0 - vec3.z
        )

        val diff = finalVelocity.subtract(deltaMovement)

        // Paper event
        val event = CraftEventFactory.callEntityKnockbackEvent(
            target.bukkitLivingEntity,
            attacker,
            attacker,
            paperCause,
            strength,
            diff
        )

        if (event.isCancelled) return

        target.hasImpulse = true
        target.deltaMovement = deltaMovement.add(
            event.knockback.x,
            event.knockback.y,
            event.knockback.z
        )
    }

    override fun push(target: Entity, x: Double, y: Double, z: Double) {
        target.toNMS().push(x, y, z)
    }

    override fun push(
        target: Entity,
        x: Double,
        y: Double,
        z: Double,
        pusher: Entity?
    ) {
        target.toNMS().push(x, y, z, pusher?.toNMS())
    }

    override fun getOnPos(entity: Entity): Vector {
        val pos = entity.toNMS().onPos
        return CraftVector.toBukkit(pos)
    }

    override fun sendSweepAttackEffects(player: Player) {
        player.toNMS().sweepAttack()
    }

    override fun sendCriticalHitEffects(player: Player, entity: Entity) {
        player.toNMS().crit(entity.toNMS())
    }

    override fun sendMagicalHitEffects(player: Player, entity: Entity) {
        player.toNMS().magicCrit(entity.toNMS())
    }

    override fun getLastHurtMob(entity: LivingEntity): Entity? {
        val lastHurtMob = entity.toNMS().lastHurtMob ?: return null
        return lastHurtMob.bukkitEntity
    }

    override fun setLastHurtMob(entity: LivingEntity, lastHurtMob: Entity) {
        entity.toNMS().setLastHurtMob(lastHurtMob.toNMS())
    }

    override fun getLastHurtMobTimestamp(entity: LivingEntity): Int {
        return entity.toNMS().lastHurtMobTimestamp
    }

    override fun getLastDamageCancelled(entity: Entity): Boolean {
        return entity.toNMS().lastDamageCancelled
    }

    override fun setLastDamageCancelled(entity: Entity, cancelled: Boolean) {
        entity.toNMS().lastDamageCancelled = cancelled
    }

    override fun getHurtMarked(entity: Entity): Boolean {
        return entity.toNMS().hurtMarked
    }

    override fun setHurtMarked(entity: Entity, marked: Boolean) {
        entity.toNMS().hurtMarked = marked
    }

    override fun sendMotionPacket(player: Player, entity: Entity) {
        player.toNMS().connection.send(
            ClientboundSetEntityMotionPacket(entity.toNMS())
        )
    }

    override fun stopSleeping(entity: LivingEntity) {
        entity.toNMS().stopSleeping()
    }

    override fun isInvulnerableToBase(
        entity: Entity,
        source: DamageSource
    ): Boolean {
        return entity.toNMS().isInvulnerableToBase(source.toNMS())
    }

    override fun isAttackable(entity: Entity): Boolean {
        return entity.toNMS().isAttackable
    }

    override fun areAllies(entity1: Entity, entity2: Entity): Boolean {
        return entity1.toNMS().isAlliedTo(entity2.toNMS())
    }

    override fun skipAttackInteraction(
        entity: Entity,
        attacker: Entity
    ): Boolean {
        return entity.toNMS().skipAttackInteraction(attacker.toNMS())
    }

    private val autoSpinAttackItemStackField = net.minecraft.world.entity.LivingEntity::class.java.getDeclaredField("autoSpinAttackItemStack").apply {
        isAccessible = true
    }

    override fun getRiptideWeapon(entity: LivingEntity): ItemStack? {
        return try {
            val itemStack = autoSpinAttackItemStackField.get(entity.toNMS()) as net.minecraft.world.item.ItemStack?
            itemStack?.asBukkitMirror()
        } catch (_: Exception) {
            null
        }
    }

    private val autoSpinAttackDmgField = net.minecraft.world.entity.LivingEntity::class.java.getDeclaredField("autoSpinAttackDmg").apply {
        isAccessible = true
    }

    override fun getRiptideDamage(entity: LivingEntity): Float {
        return try {
            autoSpinAttackDmgField.getFloat(entity.toNMS())
        } catch (_: Exception) {
            0f
        }
    }

    override fun getLastDamageCause(entity: Entity): EntityDamageEvent? {
        return entity.lastDamageCause
    }

    @Suppress("DEPRECATION")
    override fun setLastDamageCause(
        entity: Entity,
        event: EntityDamageEvent?
    ) {
        entity.lastDamageCause = event
    }

    override fun getCurrentImpulseImpactPosition(player: Player): Vector? {
        return CraftVector.toBukkit(player.toNMS().currentImpulseImpactPos)
    }

    override fun setCurrentImpulseImpactPosition(player: Player, position: Vector?) {
        player.toNMS().currentImpulseImpactPos = CraftVector.toVec3(position)
    }

    override fun isIgnoringFallDamageFromCurrentImpulse(player: Player): Boolean {
        return player.toNMS().isIgnoringFallDamageFromCurrentImpulse
    }

    override fun setIgnoreFallDamageFromCurrentImpulse(player: Player, ignore: Boolean) {
        player.toNMS().setIgnoreFallDamageFromCurrentImpulse(ignore)
    }

    override fun setSpawnExtraParticlesOnFall(player: Player, spawn: Boolean) {
        player.toNMS().setSpawnExtraParticlesOnFall(spawn)
    }

    @Suppress("UnstableApiUsage")
    override fun handleRedirectableProjectile(
        attacker: LivingEntity,
        target: Entity,
        source: DamageSource,
        damage: Float
    ): Boolean {

        val target = target.toNMS()
        val attacker = attacker.toNMS()
        val source = source.toNMS()

        if (target.type.`is`(net.minecraft.tags.EntityTypeTags.REDIRECTABLE_PROJECTILE) &&
            target is net.minecraft.world.entity.projectile.Projectile
        ) {
            val projectile = target

            if (CraftEventFactory.handleNonLivingEntityDamageEvent(
                    target,
                    source,
                    damage.toDouble(),
                    false
                )
            ) {
                return true
            }

            if (
                projectile.deflect(
                    net.minecraft.world.entity.projectile.ProjectileDeflection.AIM_DEFLECT,
                    attacker,
                    attacker,
                    true
                )
            ) {
                attacker.level().playSound(
                    null,
                    attacker.x,
                    attacker.y,
                    attacker.z,
                    net.minecraft.sounds.SoundEvents.PLAYER_ATTACK_NODAMAGE,
                    attacker.soundSource
                )
                return true
            }
        }

        return false
    }

    override fun sendSoundEffect(
        fromEntity: Player,
        x: Double,
        y: Double,
        z: Double,
        sound: Sound,
        category: SoundCategory,
        volume: Float,
        pitch: Float
    ) {
        val player =  fromEntity.toNMS()
        val sound = sound.toNMS()
        val category = category.toNMS()

        player.level().playSound(player, x, y, z, sound, category, volume, pitch)
        player.connection.send(ClientboundSoundPacket(SOUND_EVENT.wrapAsHolder(sound), category, x, y, z, volume, pitch, player.random.nextLong()))
    }

    override fun awardDamageDealt(player: Player, amount: Int) {
        player.toNMS().awardStat(Stats.DAMAGE_DEALT, amount)
    }

    override fun causeFoodExhaustion(
        player: Player,
        exhaustion: Float,
        reason: EntityExhaustionEvent.ExhaustionReason?
    ) {
        player.toNMS().let {
            if (reason != null) {
                it.causeFoodExhaustion(exhaustion, reason)
            } else {
                it.causeFoodExhaustion(exhaustion)
            }
        }
    }
}
