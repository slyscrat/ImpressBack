package com.slyscrat.impress.exception;

public class InnerLogicException extends RuntimeException {
    public InnerLogicException(String s) {
        super(s);
    }

    public InnerLogicException(Class<?> clazz, String method, String message) {
        super("Error occurred in [" + clazz.getName() + "." + method + "()]: " + message);
    }

    public InnerLogicException(Class<?> clazz, String method, Throwable throwable) {
        super("Error occurred in [" + clazz.getName() + "." + method + "()]", throwable);
    }
}
