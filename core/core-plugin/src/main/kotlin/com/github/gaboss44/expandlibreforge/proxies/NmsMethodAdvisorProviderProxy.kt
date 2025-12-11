package com.github.gaboss44.expandlibreforge.proxies

import com.github.gaboss44.expandlibreforge.features.advisor.core.MethodAdvisor
import com.github.gaboss44.expandlibreforge.features.advisor.core.MethodAdvisorProvider

interface NmsMethodAdvisorProviderProxy : MethodAdvisorProvider {

    override fun <MA: MethodAdvisor<*>> getAdvisor(type: Class<MA>): MA?
}
