package com.github.gaboss44.expandlibreforge.proxies

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

interface ItemAccessorProxy {

    fun isWeapon(stack: ItemStack): Boolean

    fun awardUsage(stack: ItemStack, player: Player)

    fun getItemDamagePerAttack(stack: ItemStack): Int

    fun hurtAndBreak(stack: ItemStack, amount: Int, user: LivingEntity, slot: EquipmentSlot)
}