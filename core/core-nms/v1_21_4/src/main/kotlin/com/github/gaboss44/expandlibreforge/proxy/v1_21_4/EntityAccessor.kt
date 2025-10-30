package com.github.gaboss44.expandlibreforge.proxy.v1_21_4

import com.github.gaboss44.expandlibreforge.proxies.EntityAccessorProxy
import net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.ai.attributes.Attributes
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.craftbukkit.CraftSound
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.craftbukkit.entity.CraftEntity
import org.bukkit.craftbukkit.entity.CraftLivingEntity
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.event.CraftEventFactory
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack

class EntityAccessor : EntityAccessorProxy {

    override fun getAttackDamage(entity: LivingEntity): Double {
        val nmsEntity = (entity as CraftLivingEntity).handle!!
        return nmsEntity.getAttributeValue(Attributes.ATTACK_DAMAGE)
    }

    override fun getSpeed(entity: LivingEntity): Double {
        val nmsEntity = (entity as CraftLivingEntity).handle!!
        return if (nmsEntity is net.minecraft.world.entity.player.Player) {
            nmsEntity.getAttributeValue(Attributes.MOVEMENT_SPEED)
        } else {
            nmsEntity.speed.toDouble()
        }
    }

    override fun isAttackable(entity: Entity): Boolean {
        return (entity as CraftEntity).handle!!.isAttackable
    }

    override fun skipAttackInteraction(
        entity: Entity,
        attacker: Entity
    ): Boolean {
        return (entity as CraftEntity).handle!!.skipAttackInteraction((attacker as CraftEntity).handle!!)
    }

    private val autoSpinAttackItemStackField = net.minecraft.world.entity.LivingEntity::class.java.getDeclaredField("autoSpinAttackItemStack").apply {
        isAccessible = true
    }

    override fun getRiptideWeapon(entity: LivingEntity): ItemStack? {
        val nmsEntity = (entity as CraftLivingEntity).handle!!
        return try {
            val itemStack = autoSpinAttackItemStackField.get(nmsEntity) as net.minecraft.world.item.ItemStack?
            itemStack?.asBukkitMirror()
        } catch (_: Exception) {
            null
        }
    }

    private val autoSpinAttackDmgField = net.minecraft.world.entity.LivingEntity::class.java.getDeclaredField("autoSpinAttackDmg").apply {
        isAccessible = true
    }

    override fun getRiptideDamage(entity: LivingEntity): Float {
        val nmsEntity = (entity as CraftLivingEntity).handle!!
        return try {
            autoSpinAttackDmgField.getFloat(nmsEntity)
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

    @Suppress("UnstableApiUsage")
    override fun handleRedirectableProjectile(
        attacker: LivingEntity,
        target: Entity,
        source: DamageSource,
        damage: Float
    ): Boolean {
        val nmsTarget = (target as CraftEntity).handle!!
        val nmsAttacker = (attacker as CraftLivingEntity).handle!!
        val nmsSource = (source as CraftDamageSource).handle

        if (nmsTarget.type.`is`(net.minecraft.tags.EntityTypeTags.REDIRECTABLE_PROJECTILE) &&
            nmsTarget is net.minecraft.world.entity.projectile.Projectile
        ) {
            val projectile = nmsTarget

            if (CraftEventFactory.handleNonLivingEntityDamageEvent(
                    nmsTarget,
                    nmsSource,
                    damage.toDouble(),
                    false
                )
            ) {
                return true
            }

            if (
                projectile.deflect(
                    net.minecraft.world.entity.projectile.ProjectileDeflection.AIM_DEFLECT,
                    nmsAttacker,
                    nmsAttacker,
                    true
                )
            ) {
                nmsAttacker.level().playSound(
                    null,
                    nmsAttacker.x,
                    nmsAttacker.y,
                    nmsAttacker.z,
                    net.minecraft.sounds.SoundEvents.PLAYER_ATTACK_NODAMAGE,
                    nmsAttacker.soundSource
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
        val nmsPlayer = (fromEntity as CraftPlayer).handle!!
        val nmsSound = CraftSound.bukkitToMinecraft(sound)!!
        val nmsCategory = SoundSource.valueOf(category.name)

        nmsPlayer.level().playSound(nmsPlayer, x, y, z, nmsSound, nmsCategory, volume, pitch)
        nmsPlayer.connection.send(ClientboundSoundPacket(SOUND_EVENT.wrapAsHolder(nmsSound), nmsCategory, x, y, z, volume, pitch, nmsPlayer.random.nextLong()))
    }
}
