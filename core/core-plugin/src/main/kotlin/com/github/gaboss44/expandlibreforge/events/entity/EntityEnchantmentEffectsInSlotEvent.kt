package com.github.gaboss44.expandlibreforge.events.entity

import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsInSlotData
import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList

open class EntityEnchantmentEffectsInSlotEvent(
    enchantmentUser: LivingEntity,
    override val enchantmentEffectsData: EnchantmentEffectsInSlotData
) : EntityEnchantmentEffectsEvent(
    enchantmentUser,
    enchantmentEffectsData
) {

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}