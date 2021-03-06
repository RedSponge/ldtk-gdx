package com.redsponge.ldtkgdx;

public class LDTKException extends RuntimeException {
    public LDTKException() {
    }

    public LDTKException(String message) {
        super(message);
    }

    public LDTKException(String message, Throwable cause) {
        super(message, cause);
    }

    public LDTKException(Throwable cause) {
        super(cause);
    }

    public LDTKException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
