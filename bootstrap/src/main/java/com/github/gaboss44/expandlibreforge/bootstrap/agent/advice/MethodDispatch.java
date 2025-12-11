package com.github.gaboss44.expandlibreforge.bootstrap.agent.advice;

public interface MethodDispatch {
    void onEnter(MethodContext context) throws Throwable;

    void onExit(MethodContext context, Object returnValue) throws Throwable;

    void onException(MethodContext context, Throwable throwable) throws Throwable;
}
