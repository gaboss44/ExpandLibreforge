package com.github.gaboss44.expandlibreforge.features.advisor.core

import com.github.gaboss44.expandlibreforge.ExpandLibreforgePlugin
import com.github.gaboss44.expandlibreforge.features.advisor.AdvisorInstaller
import com.github.gaboss44.expandlibreforge.features.advisor.nms.player.NmsPlayerAttackMethodAdvisor
import java.lang.instrument.Instrumentation

object MethodAdvisorBootstrapper {

    fun bootstrap(plugin: ExpandLibreforgePlugin, instrumentation: Instrumentation, provider: MethodAdvisorProvider) {
        val installer = AdvisorInstaller(plugin, instrumentation)
        val advisors = mutableListOf<MethodAdvisor<*>>()

        provider.getAdvisor(
            NmsPlayerAttackMethodAdvisor::class.java
        )?.let { advisor ->
            advisor.addConsumer { it.payload.shouldPerformAttack = true }
            advisors.add(advisor)
        }

        installer install advisors.toList()
    }
}
