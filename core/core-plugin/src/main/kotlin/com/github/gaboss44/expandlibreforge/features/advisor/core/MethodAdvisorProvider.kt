package com.github.gaboss44.expandlibreforge.features.advisor.core

interface MethodAdvisorProvider {

    fun <MA : MethodAdvisor<*>> getAdvisor(type: Class<MA>): MA?

    interface Combined: MethodAdvisorProvider, Iterable<MethodAdvisorProvider> {

        override fun <MA : MethodAdvisor<*>> getAdvisor(type: Class<MA>): MA? {
            for (provider in this) {
                val advisor = provider.getAdvisor(type)
                if (advisor != null) {
                    return advisor
                }
            }
            return null
        }

        companion object {

            fun of(provider: MethodAdvisorProvider): Combined = Impl(listOf(provider))

            fun of(vararg providers: MethodAdvisorProvider): Combined = Impl(providers.toList())

            fun of(providers: List<MethodAdvisorProvider>): Combined = Impl(providers)
        }

        private class Impl(private val providers: List<MethodAdvisorProvider>): Combined {

            override fun iterator() = providers.iterator()
        }
    }
}
