package com.bnyte.forge.exception;

/**
 * @auther bnyte
 * @date 2021-12-09 19:20
 * @email bnytezz@gmail.com
 */
public class ForgeException extends RuntimeException {
    public ForgeException() {
    }

    public ForgeException(String message) {
        super(message);
    }

    public ForgeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForgeException(Throwable cause) {
        super(cause);
    }

    public ForgeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
