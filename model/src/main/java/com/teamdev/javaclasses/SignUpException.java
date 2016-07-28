package com.teamdev.javaclasses;

/**
 * Throws if sign up fail.
 */
public class SignUpException extends Exception {
    /*TODO add enum to parametrize messages*/
    public SignUpException(String message) {
        super(message);
    }

    public SignUpException(String message, Throwable cause) {
        super(message, cause);
    }
}
