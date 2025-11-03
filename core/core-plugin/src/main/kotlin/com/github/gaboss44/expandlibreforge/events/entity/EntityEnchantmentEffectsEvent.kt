package com.github.gaboss44.expandlibreforge.events.entity

import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsData
import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityEvent

open class EntityEnchantmentEffectsEvent(
    open val enchantmentUser: LivingEntity,
    open val enchantmentEffectsData: EnchantmentEffectsData
) : EntityEvent(enchantmentUser) {

    override fun getEntity(): LivingEntity {
        return this.enchantmentUser
    }

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}