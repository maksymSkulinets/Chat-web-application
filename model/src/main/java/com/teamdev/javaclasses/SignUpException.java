package com.teamdev.javaclasses;

/**
 * Throws if sign up fail.
 */
class SignUpException extends Exception {
    public SignUpException(String message) {
        super(message);
    }

    public SignUpException(String message, Throwable cause) {
        super(message, cause);
    }
}
