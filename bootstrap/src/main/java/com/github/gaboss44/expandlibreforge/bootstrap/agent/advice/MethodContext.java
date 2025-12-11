package com.github.gaboss44.expandlibreforge.bootstrap.agent.advice;

public record MethodContext(Object instance, String methodName, Object[] arguments) {
}
