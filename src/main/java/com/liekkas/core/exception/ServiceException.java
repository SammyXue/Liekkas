package com.liekkas.core.exception;

/**
 * 业务层抛出错误Exception
 *
 * @author xuecm
 */
public class ServiceException extends RuntimeException {
    private String errorMsg;

    public ServiceException(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
