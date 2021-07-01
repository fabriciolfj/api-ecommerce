package com.github.fabriciolfj.apiecommerce.exceptions;

public class ErrorUtils {

    private ErrorUtils() {

    }

    public static Error createError(final String errMsgKey, final String errorCode, final Integer httpStatusCode) {
        return Error.builder()
                .message(errMsgKey)
                .errorCode(errorCode)
                .status(httpStatusCode)
                .build();
    }
}
