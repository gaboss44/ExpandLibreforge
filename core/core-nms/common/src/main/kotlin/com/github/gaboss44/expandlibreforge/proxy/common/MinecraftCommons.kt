@file:Suppress("UnstableApiUsage")
package com.github.gaboss44.expandlibreforge.proxy.common

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.World
import org.bukkit.damage.DamageSource
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

private lateinit var commonsProvider: CommonsProvider

fun World.toNMS(): ServerLevel =
    commonsProvider.toNMS(this)

fun Player.toNMS(): ServerPlayer =
    commonsProvider.toNMS(this)

fun Entity.toNMS(): net.minecraft.world.entity.Entity =
    commonsProvider.toNMS(this)

fun LivingEntity.toNMS(): net.minecraft.world.entity.LivingEntity =
    commonsProvider.toNMS(this)

fun AbstractArrow.toNMS(): net.minecraft.world.entity.projectile.AbstractArrow =
    commonsProvider.toNMS(this)

fun DamageSource.toNMS(): net.minecraft.world.damagesource.DamageSource =
    commonsProvider.toNMS(this)

fun Sound.toNMS(): SoundEvent =
    commonsProvider.toNMS(this)

fun SoundCategory.toNMS(): SoundSource =
    commonsProvider.toNMS(this)

fun ItemStack.asNMSCopy(): net.minecraft.world.item.ItemStack =
    commonsProvider.asNMSCopy(this)

fun ItemStack.unwrapNMS(): net.minecraft.world.item.ItemStack =
    commonsProvider.unwrapNMS(this)

interface CommonsProvider {

    fun toNMS(world: World): ServerLevel

    fun toNMS(player: Player): ServerPlayer

    fun toNMS(entity: Entity): net.minecraft.world.entity.Entity

    fun toNMS(entity: LivingEntity): net.minecraft.world.entity.LivingEntity

    fun toNMS(arrow: AbstractArrow): net.minecraft.world.entity.projectile.AbstractArrow

    fun toNMS(source: DamageSource): net.minecraft.world.damagesource.DamageSource

    fun toNMS(sound: Sound): SoundEvent

    fun toNMS(category: SoundCategory): SoundSource

    fun asNMSCopy(stack: ItemStack): net.minecraft.world.item.ItemStack

    fun unwrapNMS(stack: ItemStack): net.minecraft.world.item.ItemStack

    companion object {

        fun setIfNeeded(provider: CommonsProvider) {
            if (!::commonsProvider.isInitialized) {
                commonsProvider = provider
            }
        }
    }
}