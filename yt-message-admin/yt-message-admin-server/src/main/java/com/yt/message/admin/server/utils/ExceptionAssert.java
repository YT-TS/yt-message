package com.yt.message.admin.server.utils;

import java.util.function.Supplier;

/**
 * @ClassName ExceptionAssert
 * @Author Ts
 * @Version 1.0
 */

public class ExceptionAssert {

    public static void throwException(Exception e) throws Exception {
        throw e;
    }

    public static void throwOnFalse(boolean expression, Exception e) throws Exception {
        if (!expression) {
            throwException(e);
        }

    }
    public static void throwOnFalse(boolean expression, Supplier<? extends Exception> exceptionSupplier) throws Exception {
        if (!expression) {
            throwException(exceptionSupplier.get());
        }

    }

}
