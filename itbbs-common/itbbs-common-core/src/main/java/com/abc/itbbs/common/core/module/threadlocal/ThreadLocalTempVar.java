package com.abc.itbbs.common.core.module.threadlocal;

public class ThreadLocalTempVar {

    public static final ThreadLocal<Object> TEMP_TOKEN_VAR = new ThreadLocal<>();

    public static Object getTempTokenVar() {
        return TEMP_TOKEN_VAR.get();
    }

    public static void setTempTokenVar(Object obj) {
        TEMP_TOKEN_VAR.set(obj);
    }

    public static void removeTempTokenVar() {
        TEMP_TOKEN_VAR.remove();
    }

}
