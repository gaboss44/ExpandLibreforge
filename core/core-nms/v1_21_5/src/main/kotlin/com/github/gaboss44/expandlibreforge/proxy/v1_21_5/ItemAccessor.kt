package com.github.gaboss44.expandlibreforge.proxy.v1_21_5

import com.github.gaboss44.expandlibreforge.proxies.ItemAccessorProxy
import com.github.gaboss44.expandlibreforge.proxy.common.asNMSCopy
import com.github.gaboss44.expandlibreforge.proxy.common.toNMS
import com.github.gaboss44.expandlibreforge.proxy.common.unwrapNMS
import net.minecraft.core.component.DataComponents
import net.minecraft.stats.Stats
import org.bukkit.craftbukkit.CraftEquipmentSlot
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class ItemAccessor : ItemAccessorProxy {

    override fun isWeapon(stack: ItemStack): Boolean {
        return stack.unwrapNMS().has(DataComponents.WEAPON)
    }

    override fun awardUsage(stack: ItemStack, player: Player) {
        player.toNMS().awardStat(Stats.ITEM_USED.get(stack.asNMSCopy().item))
    }

    override fun getItemDamagePerAttack(stack: ItemStack): Int {
        return stack.unwrapNMS().get(DataComponents.WEAPON)?.itemDamagePerAttack() ?: 0
    }

    override fun hurtAndBreak(
        stack: ItemStack,
        amount: Int,
        user: LivingEntity,
        slot: EquipmentSlot
    ){
        stack.unwrapNMS().hurtAndBreak(amount, user.toNMS(), CraftEquipmentSlot.getNMS(slot))
    }
}