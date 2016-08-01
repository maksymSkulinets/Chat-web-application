package com.teamdev.javaclasses;

/**
 * Throws if login fail.
 */
public class LoginException extends Exception {
    public LoginException(UserServiceFailCases failCase) {
        super(failCase.getMessage());
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
