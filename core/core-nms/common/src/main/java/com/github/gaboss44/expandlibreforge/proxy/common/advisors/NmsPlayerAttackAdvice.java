package com.github.gaboss44.expandlibreforge.proxy.common.advisors;

import com.github.gaboss44.expandlibreforge.ExpandLibreforgePlugin;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner.Typing;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.logging.Level;

public class NmsPlayerAttackAdvice {

    public static class EnterResult {
        public final Object context;
        public boolean skip;
        public Object returnValue;

        public EnterResult(Object context, boolean skip, Object returnValue) {
            this.context = context;
            this.skip = skip;
            this.returnValue = returnValue;
        }
    }

    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    public static EnterResult onEnter(
            @Advice.This Object instance,
            @Advice.AllArguments Object[] args,
            @Advice.Origin Method method) {

        NmsPlayerAttackMethodAdvisorImpl advisor = NmsPlayerAttackMethodAdvisorImpl.Companion.getInstanceOrNull();
        if (advisor == null) {
            return null;
        }

        NmsPlayerAttackMethodAdvisorImpl.Context ctx = advisor.getContextFactory().create(args, instance, method);

        if (ctx == null) {
            return null;
        }

        for (Consumer<NmsPlayerAttackMethodAdvisorImpl.Context> c : advisor.getConsumers()) {
            try {
                c.accept(ctx);
            } catch (Exception ex) {
                ExpandLibreforgePlugin.instance().getLogger().log(
                        Level.SEVERE,
                        "Could not pass context " + ctx + " to consumer " + c + ".",
                        ex
                );
            }
        }

        if (ctx.getPayload().getShouldPerformAttack()) {
            return new EnterResult(ctx, true, null);
        }

        return null;
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void onExit(
            @Advice.Enter EnterResult enter,
            @Advice.Return(readOnly = false, typing = Typing.DYNAMIC) Object ret
    ) {
        if (enter != null && enter.skip) {
            ret = enter.returnValue;
        }
    }
}
