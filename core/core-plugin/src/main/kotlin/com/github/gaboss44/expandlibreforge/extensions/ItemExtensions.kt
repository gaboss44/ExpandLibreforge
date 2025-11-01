package com.github.gaboss44.expandlibreforge.extensions

import com.github.gaboss44.expandlibreforge.proxies.ItemAccessorProxy
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

private lateinit var itemAccessor: ItemAccessorProxy

object ItemExtensions {

    fun setProxyIfNeeded(itemAccessorProxy: ItemAccessorProxy) {
        itemAccessor = itemAccessorProxy
    }
}

val ItemStack.isWeapon : Boolean
    get() = itemAccessor.isWeapon(this)

fun ItemStack.awardUsage(player: Player) {
    itemAccessor.awardUsage(this, player)
}

val ItemStack.itemDamagePerAttack
    get() = itemAccessor.getItemDamagePerAttack(this)

fun ItemStack.hurtAndBreak(amount: Int, user: LivingEntity, slot: EquipmentSlot) {
    itemAccessor.hurtAndBreak(this, amount, user, slot)
}