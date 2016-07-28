package com.teamdev.javaclasses;

/**
 * Throws if login fail.
 */
public class LoginException extends Exception {
        /*TODO add enum to parametrize messages*/
    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
