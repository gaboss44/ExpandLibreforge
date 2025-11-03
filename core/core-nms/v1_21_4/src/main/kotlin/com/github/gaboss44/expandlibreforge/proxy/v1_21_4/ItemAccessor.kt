package com.github.gaboss44.expandlibreforge.proxy.v1_21_4

import com.github.gaboss44.expandlibreforge.proxies.ItemAccessorProxy
import com.github.gaboss44.expandlibreforge.proxy.common.asNMSCopy
import com.github.gaboss44.expandlibreforge.proxy.common.toNMS
import com.github.gaboss44.expandlibreforge.proxy.common.unwrapNMS
import net.minecraft.stats.Stats
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.MaceItem
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.TridentItem
import org.bukkit.craftbukkit.CraftEquipmentSlot
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class ItemAccessor : ItemAccessorProxy {

    val net.minecraft.world.item.ItemStack.isWeapon get() =
        when (this.item) {
            is SwordItem,
            is MaceItem,
            is DiggerItem,
            is TridentItem, -> true
            else -> false
        }

    val damagesPerAttack = mapOf(
        SwordItem::class.java to 1,
        MaceItem::class.java to 1,
        DiggerItem::class.java to 2,
        TridentItem::class.java to 1
    )

    override fun isWeapon(stack: ItemStack): Boolean {
        return stack.asNMSCopy().isWeapon
    }

    override fun awardUsage(stack: ItemStack, player: Player) {
        player.toNMS().awardStat(Stats.ITEM_USED.get(stack.asNMSCopy().item))
    }

    override fun getItemDamagePerAttack(stack: ItemStack): Int {
        return damagesPerAttack[stack.unwrapNMS().item.javaClass] ?: 0
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