package com.github.gaboss44.expandlibreforge

import com.github.gaboss44.expandlibreforge.extensions.*
import com.github.gaboss44.expandlibreforge.conditions.*
import com.github.gaboss44.expandlibreforge.effects.*
import com.github.gaboss44.expandlibreforge.features.combo.ComboPlaceholder
import com.github.gaboss44.expandlibreforge.features.placeholder.ExpandLibreforgePlaceholder
import com.github.gaboss44.expandlibreforge.filters.*
import com.github.gaboss44.expandlibreforge.triggers.*
import com.github.gaboss44.expandlibreforge.mutators.*
import com.github.gaboss44.expandlibreforge.integrations.*
import com.github.gaboss44.expandlibreforge.listeners.*
import com.github.gaboss44.expandlibreforge.proxies.*
import com.willfp.eco.core.LifecyclePosition
import com.willfp.eco.core.Prerequisite
import com.willfp.eco.util.ClassUtils
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.mutators.Mutators
import com.willfp.libreforge.triggers.Triggers
import net.bytebuddy.agent.ByteBuddyAgent
import org.bukkit.event.Listener
import java.lang.instrument.ClassFileTransformer
import java.lang.instrument.Instrumentation
import java.security.ProtectionDomain
import java.util.jar.JarFile
import java.util.logging.Level
import java.util.logging.Logger

class ExpandLibreforgePlugin : LibreforgePlugin() {

    private var instrumentationFeaturesEnabled = false

    private var instrumentation: Instrumentation? = null

    private var dynamicAgentLoadingSucceeded = false

    private var dynamicAgentCanTransform = false

    private var bootstrapInitialized = false

    init {
        initInstance(this)

        this.onLoad(LifecyclePosition.START) {
            if (!instrumentationFeaturesEnabled) return@onLoad

            val instrumentation = tryInstallByteBuddyAgent()
            if (instrumentation != null) {
                dynamicAgentLoadingSucceeded = true
                if (testDynamicAgentBukkitClass(instrumentation)) {
                    dynamicAgentCanTransform = true
                }
            }
            this.instrumentation = instrumentation
        }

        this.onLoad(LifecyclePosition.END) {
            val instrumentation = getValidInstrumentation()
            if (instrumentation != null) {
                this.bootstrapInitialized = installBootstrapJar(instrumentation)
            }
        }
    }

    private fun installBootstrapJar(instrumentation: Instrumentation): Boolean {
        try {
            val internalPath = "bootstrap/expandlibreforge-bootstrap.jar"

            val input = this.javaClass.classLoader.getResourceAsStream(internalPath)
                ?: throw IllegalStateException("Could not find $internalPath inside plugin JAR")

            val tempBootstrap = kotlin.io.path.createTempFile(
                prefix = "expandlibreforge-bootstrap-",
                suffix = ".jar"
            ).toFile()

            tempBootstrap.deleteOnExit()

            input.use { ins ->
                tempBootstrap.outputStream().use { outs ->
                    ins.copyTo(outs)
                }
            }

            logger.info("Created temporary bootstrap jar: ${tempBootstrap.absolutePath}")

            val jar = JarFile(tempBootstrap)
            instrumentation.appendToBootstrapClassLoaderSearch(jar)

            logger.info("Added bootstrap jar to bootstrap classloader")

            val clazz = Class.forName(
                "com.github.gaboss44.expandlibreforge.bootstrap.BootstrapInitializer",
                true,
                null
            )

            val method = clazz.getMethod(
                "initialize",
                Logger::class.java
            )

            method.invoke(null, logger)

            logger.info("BootstrapInitializer invoked successfully")

        } catch (ex: Exception) {
            logger.log(Level.SEVERE, "Failed to install bootstrap jar", ex)
            return false
        }

        return true
    }

    private fun logExceptionFromAdvice(t: Throwable) {
        logger.severe("Exception thrown inside ByteBuddy advice: ${t::class.java.name}: ${t.message}")
        t.printStackTrace()
    }

    private fun tryInstallByteBuddyAgent(): Instrumentation? {
        try {
            val instrumentation = ByteBuddyAgent.install()
            logger.info("Successfully tested ByteBuddy Instrumentation availability")
            return instrumentation
        } catch (t: Throwable) {
            logger.log(Level.WARNING, "Could not get ByteBuddy Instrumentation", t)
            return null
        }
    }

    private fun testDynamicAgentBukkitClass(instrumentation: Instrumentation): Boolean {
        try {
            val transformer = object : ClassFileTransformer {
                override fun transform(
                    loader: ClassLoader?,
                    className: String?,
                    classBeingRedefined: Class<*>?,
                    protectionDomain: ProtectionDomain?,
                    classfileBuffer: ByteArray?
                ): ByteArray? {
                    if (className == "org/bukkit/entity/Player") {
                        logger.info("Transformer invoked for Player (test only)")
                    }
                    return null
                }
            }

            instrumentation.addTransformer(transformer, true)

            val clazz = org.bukkit.entity.Player::class.java

            logger.info("Attempting retransformation: ${clazz.name}")

            instrumentation.retransformClasses(clazz)

            logger.info("Retransformation finished successfully")

            return true

        } catch (ex: Throwable) {
            logger.severe("Retransformation failed using dynamic agent")
            logger.log(Level.WARNING, ex.message, ex)

            return false
        }
    }

    private fun getValidInstrumentation(): Instrumentation? {
        val instrumentation = instrumentation
        if (instrumentation != null && dynamicAgentLoadingSucceeded && dynamicAgentCanTransform) {
            return instrumentation
        }
        return null
    }

    companion object {

        @JvmStatic
        private lateinit var INSTANCE: ExpandLibreforgePlugin

        @JvmStatic
        fun instance(): ExpandLibreforgePlugin {
            if (!::INSTANCE.isInitialized) {
                throw IllegalStateException("Tried to access ExpandLibreforgePlugin instance when not yet initialized!")
            }
            return INSTANCE
        }

        @JvmStatic
        private fun initInstance(instance: ExpandLibreforgePlugin) {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = instance
            }
        }
    }

    override fun handleEnable() {

        getProxy(CommonsInitializerProxy::class.java).init(this)
        MinecraftMathFunctions.setProxyIfNeeded(getProxy(MinecraftMathProxy::class.java))
        EnchantmentHelpers.setProxyIfNeeded(getProxy(EnchantmentHelperProxy::class.java))
        DamageSourceExtensions.setProxyIfNeeded(getProxy(DamageSourceAccessorProxy::class.java))
        ItemExtensions.setProxyIfNeeded(getProxy(ItemAccessorProxy::class.java))
        EntityExtensions.setProxyIfNeeded(getProxy(EntityAccessorProxy::class.java))
        WorldExtensions.setProxyIfNeeded(getProxy(WorldAccessorProxy::class.java))

        Effects.register(EffectPlaySoundKey)
        Effects.register(EffectPlaySoundKeyToWorld)

        Effects.register(EffectSetGravity)
        Effects.register(EffectSetSilent)

        Effects.register(EffectSetNoDamageTicks)

        Effects.register(EffectAntigravity)
        Effects.register(EffectInvulnerability)
        Effects.register(EffectSilence)

        Effects.register(EffectSetXpChange)

        Effects.register(EffectMultiplyProjectileVelocity)

        Effects.register(EffectStartCombo)
        Effects.register(EffectEndCombo)
        Effects.register(EffectExtendCombo)
        Effects.register(EffectStartOrExtendCombo)

        Effects.register(EffectSetComboRenewalTicks)
        Effects.register(EffectSetComboUpdateTicks)
        Effects.register(EffectSetComboStartTicks)

        Effects.register(EffectSetComboTriggerShouldUpdateEffects)

        Effects.register(EffectSetDamageModifier)

        Effects.register(EffectPutDamageMultiplier)

        Effects.register(EffectSetDamageTakenByItem)
        Effects.register(EffectRejectDamageTakenByItemUsingPoisson)
        Effects.register(EffectRejectDamageTakenByItemDiscreetly)

        Effects.register(EffectArmorMultiplier)
        Effects.register(EffectArmorToughnessMultiplier)

        Effects.register(EffectSetExhaustion)

        Effects.register(EffectSetConsumedItem)
        Effects.register(EffectSetConsumeReplacement)

        Effects.register(EffectRepeatAttackFromDamageEvent)

        Effects.register(EffectAttackVictim)

        Effects.register(EffectSwingMainHand)
        Effects.register(EffectSwingOffHand)

        Effects.register(EffectPerformAttack)

        Effects.register(EffectReformulateDamageModifiers)
        Effects.register(EffectSetDamageArmorAbsorbReformulation)
        Effects.register(EffectSetDamageMagicAbsorbReformulation)

        Effects.register(EffectSetEnchantmentEffectsData)

        Triggers.register(TriggerRiptide)
        Triggers.register(TriggerInteract(this))
        Triggers.register(TriggerInventoryInteract)
        Triggers.register(TriggerInventoryClick)
        Triggers.register(TriggerMineBlockStop)

        TriggerXpChange.registerAllInto(Triggers)

        TriggerTakeDamage.registerAllInto(Triggers)
        TriggerInflictDamage.registerAllInto(Triggers)

        TriggerComboStart.registerAllInto(Triggers)
        TriggerComboEnd.registerAllInto(Triggers)
        TriggerComboTick.registerAllInto(Triggers)

        TriggerOfflineComboEnd.registerAllInto(Triggers)
        TriggerOfflineComboTick.registerAllInto(Triggers)

        Triggers.register(TriggerToggleShield)

        TriggerProjectileHits.registerAllInto(Triggers)
        TriggerHitByProjectile.registerAllInto(Triggers)

        TriggerExhaustion.registerAllInto(Triggers)

        TriggerInflictSmashAttackAttempt.registerAllInto(Triggers)
        TriggerInflictCriticalAttackAttempt.registerAllInto(Triggers)
        TriggerTakeSmashAttackAttempt.registerAllInto(Triggers)
        TriggerTakeCriticalAttackAttempt.registerAllInto(Triggers)

        TriggerEnchantmentArmorEffectivenessEffectsAsUser.registerAllInto(Triggers)
        TriggerEnchantmentArmorEffectivenessEffectsAsVictim.registerAllInto(Triggers)

        TriggerEnchantmentDamageEffectsAsUser.registerAllInto(Triggers)
        TriggerEnchantmentDamageEffectsAsVictim.registerAllInto(Triggers)

        TriggerEnchantmentFallBasedDamageEffectsAsUser.registerAllInto(Triggers)
        TriggerEnchantmentFallBasedDamageEffectsAsVictim.registerAllInto(Triggers)

        TriggerEnchantmentKnockbackEffectsAsUser.registerAllInto(Triggers)
        TriggerEnchantmentKnockbackEffectsAsVictim.registerAllInto(Triggers)

        TriggerEnchantmentPostAttackBySlotEffectsAsUser.registerAllInto(Triggers)
        TriggerEnchantmentPostAttackBySlotEffectsAsVictim.registerAllInto(Triggers)

        TriggerEnchantmentPostAttackByWeaponEffectsAsUser.registerAllInto(Triggers)
        TriggerEnchantmentPostAttackByWeaponEffectsAsVictim.registerAllInto(Triggers)

        Mutators.register(MutatorAttackDamageAsValue)
        Mutators.register(MutatorAttackDamageAsAltValue)

        Mutators.register(MutatorAttackFinalDamageAsValue)
        Mutators.register(MutatorAttackFinalDamageAsAltValue)

        Mutators.register(MutatorXpChangeAsValue)
        Mutators.register(MutatorXpChangeAsAltValue)

        MutatorComboOwner.registerAllInto(Mutators)

        Conditions.register(ConditionHasCombo)

        Filters.register(FilterInventoryAction)
        Filters.register(FilterInventoryDragType)
        Filters.register(FilterInventoryInteractType)
        Filters.register(FilterPlayerAction)
        Filters.register(FilterTargetReason)

        Filters.register(FilterPlayerIsPresent)
        Filters.register(FilterPlayerIsSilent)
        Filters.register(FilterPlayerIsInvulnerable)
        Filters.register(FilterPlayerHasGravity)

        Filters.register(FilterIsSprinting)
        Filters.register(FilterIsSneaking)
        Filters.register(FilterIsBlocking)
        Filters.register(FilterLocationOnGround)
        Filters.register(FilterEntityOnGround)

        Filters.register(FilterVictimIsPresent)
        Filters.register(FilterMatchEntitiesIfPresent)
        Filters.register(FilterIgnoreEntitiesIfPresent)

        Filters.register(FilterProjectileIsPresent)
        Filters.register(FilterMatchProjectilesIfPresent)
        Filters.register(FilterIgnoreProjectilesIfPresent)

        Filters.register(FilterDamagerIsPresent)

        FilterTradeSelectIndex.registerAllInto(Filters)

        FilterPlayerNoDamageTicks.registerAllInto(Filters)

        FilterVictimNoDamageTicks.registerAllInto(Filters)

        FilterXpChange.registerAllInto(Filters)

        FilterXpOrbCount.registerAllInto(Filters)

        FilterXpOrbExperience.registerAllInto(Filters)

        Filters.register(FilterMatchXpOrbSpawnReasonIfPresent)
        Filters.register(FilterIgnoreXpOrbSpawnReasonIfPresent)

        Filters.register(FilterHasAnyCombo)

        Filters.register(FilterComboIsPresent)
        Filters.register(FilterMatchComboIfPresent)
        Filters.register(FilterIgnoreComboIfPresent)
        Filters.register(FilterHasCombos)

        FilterComboCount.registerAllInto(Filters)
        FilterComboScore.registerAllInto(Filters)
        FilterComboRemainingTicks.registerAllInto(Filters)

        FilterPlayerAttackCooldown.registerAllInto(Filters)
        FilterVictimAttackCooldown.registerAllInto(Filters)

        Filters.register(FilterVictimIsHumanEntity)
        Filters.register(FilterVictimIsPlayer)

        FilterDamageTakenByItem.registerAllInto(Filters)
        FilterOriginalDamageTakenByItem.registerAllInto(Filters)

        Filters.register(FilterDamagedItem)

        Filters.register(FilterDamageType)
        Filters.register(FilterDamageSourceIsIndirect)

        Filters.register(FilterMatchExhaustionReasonIfPresent)
        Filters.register(FilterIgnoreExhaustionReasonIfPresent)
        FilterExhaustion.registerAllInto(Filters)

        Filters.register(FilterMatchConsumeReplacementIfPresent)
        Filters.register(FilterIgnoreConsumeReplacementIfPresent)

        FilterFallDistance.registerAllInto(Filters)
        FilterVictimFallDistance.registerAllInto(Filters)

        Filters.register(FilterPlayerSameAsVictim)

        Filters.register(FilterItemsInSlot)

        Filters.register(FilterMatchComboPhaseIfPresent)
        Filters.register(FilterIgnoreComboPhaseIfPresent)

        Filters.register(FilterEnchantmentEffectsData)

        if (Prerequisite.HAS_PAPER.isMet) {
            PaperIntegration.load(this)
        }

        if (ClassUtils.exists("org.bukkit.Input")) {
            Effects.register(EffectSetPlayerInputShouldUpdateEffects)

            Triggers.register(TriggerPlayerInput)

            FilterPlayerInput.registerAllInto(Filters)

            Conditions.register(ConditionPlayerCurrentInput)
        }

        ComboPlaceholder.createHas(this).register()
        ComboPlaceholder.createCount(this).register()
        ComboPlaceholder.createScore(this).register()
        ComboPlaceholder.createRemainingTicks(this).register()
        ComboPlaceholder.createInitialTicks(this).register()
        ComboPlaceholder.createMaximumTicks(this).register()
        ComboPlaceholder.createCompletedTicks(this).register()

        ExpandLibreforgePlaceholder.createFallDistance(this).register()

//        val instrumentation = tryInstallByteBuddyAgent()
//        if (instrumentation != null) {
//            Props.dynamicAgentLoadingSucceeded = true
//            if (testDynamicAgentBukkitClass(instrumentation)) {
//                Props.dynamicAgentCanTransform = true
//            }
//        }
//
//        if (instrumentation != null && Props.dynamicAgentLoadingSucceeded && Props.dynamicAgentCanTransform) {
//            try {
//                logger.info("Bootstrapping the method advisors...")
//                val provider = MethodAdvisorProvider.Combined.of(
//                    getProxy(NmsMethodAdvisorProviderProxy::class.java)
//                )
//                MethodAdvisorBootstrapper.bootstrap(this, instrumentation, provider)
//                logger.info("Successfully bootstrapped the method advisors")
//            } catch (t: Throwable) {
//                logger.log(Level.WARNING, "Could not bootstrap the method advisors", t)
//            }
//        }
    }

    override fun loadListeners(): List<Listener> {
        val listeners = mutableListOf<Listener>()
        if (Prerequisite.HAS_PAPER.isMet) {
            listeners.add(ServerTickListener)
        }
        if (ClassUtils.exists("org.bukkit.Input")) {
            listeners.add(PlayerInputListener)
        }
        listeners.add(DamageListener)
        listeners.add(PlayerJoinListener)
        listeners.add(PlayerQuitListener)
        return listeners.toList()
    }
}
