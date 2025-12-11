package com.github.gaboss44.expandlibreforge.proxy.v1_21_4

import com.github.gaboss44.expandlibreforge.features.advisor.core.MethodAdvisor
import com.github.gaboss44.expandlibreforge.features.advisor.nms.player.NmsPlayerAttackMethodAdvisor
import com.github.gaboss44.expandlibreforge.proxies.NmsMethodAdvisorProviderProxy
import com.github.gaboss44.expandlibreforge.proxy.common.advisors.NmsPlayerAttackMethodAdvisorImpl

class NmsMethodAdvisorProvider : NmsMethodAdvisorProviderProxy {

    private val suppliers: Map<Class<out MethodAdvisor<*>>, () -> MethodAdvisor<*>> =
        mapOf(
            NmsPlayerAttackMethodAdvisor::class.java to NmsPlayerAttackMethodAdvisorImpl::getInstance
        )

    override fun <MA : MethodAdvisor<*>> getAdvisor(type: Class<MA>): MA? {
        val supplier = suppliers[type] ?: return null
        val instance = supplier()

        if (!type.isInstance(instance)) {
            return null
        }

        @Suppress("UNCHECKED_CAST")
        return instance as MA
    }
}
