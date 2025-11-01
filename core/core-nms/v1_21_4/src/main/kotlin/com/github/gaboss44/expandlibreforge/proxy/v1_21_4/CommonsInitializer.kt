package com.github.gaboss44.expandlibreforge.proxy.v1_21_4

import com.github.gaboss44.expandlibreforge.proxies.CommonsInitializerProxy
import com.github.gaboss44.expandlibreforge.proxy.common.CommonsProvider
import com.willfp.eco.core.EcoPlugin
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.World
import org.bukkit.craftbukkit.CraftSound
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.craftbukkit.entity.CraftEntity
import org.bukkit.craftbukkit.entity.CraftLivingEntity
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Player

class CommonsInitializer : CommonsInitializerProxy {

    override fun init(plugin: EcoPlugin) {
        CommonsProvider.setIfNeeded(CommonsProviderImpl)
    }

    object CommonsProviderImpl : CommonsProvider {
        override fun toNMS(world: World): ServerLevel {
            return (world as CraftWorld).handle
        }

        override fun toNMS(player: Player): ServerPlayer {
            return (player as CraftPlayer).handle
        }

        override fun toNMS(entity: org.bukkit.entity.Entity): Entity {
            return (entity as CraftEntity).handle
        }

        override fun toNMS(entity: org.bukkit.entity.LivingEntity): LivingEntity {
            return (entity as CraftLivingEntity).handle
        }

        override fun toNMS(source: org.bukkit.damage.DamageSource): DamageSource {
            return (source as CraftDamageSource).handle
        }

        override fun toNMS(sound: Sound): SoundEvent {
            return CraftSound.bukkitToMinecraft(sound)
        }

        override fun toNMS(category: SoundCategory): SoundSource {
            return SoundSource.valueOf(category.name)
        }

        override fun asNMSCopy(stack: org.bukkit.inventory.ItemStack): ItemStack {
            return CraftItemStack.asNMSCopy(stack)
        }

        override fun unwrapNMS(stack: org.bukkit.inventory.ItemStack): ItemStack {
            return CraftItemStack.unwrap(stack)
        }
    }
}