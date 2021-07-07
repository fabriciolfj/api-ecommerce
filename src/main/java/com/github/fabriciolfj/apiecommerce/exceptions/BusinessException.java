package com.github.fabriciolfj.apiecommerce.exceptions;

public abstract class BusinessException extends RuntimeException {

    public BusinessException(final String msg) {
        super(msg);
    }

    public abstract String getErrMsgKey();

    public abstract String getErrorCode();

}
