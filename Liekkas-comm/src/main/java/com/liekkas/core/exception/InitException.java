package com.liekkas.core.exception;

/**
 * 初始化时抛出的
 */
public class InitException extends Exception {
    private Exception exception;

    public InitException(Exception exception, String clzName) {
        super("please check initService in " + clzName);
    }



    public Exception getException() {
        return exception;
    }
}
