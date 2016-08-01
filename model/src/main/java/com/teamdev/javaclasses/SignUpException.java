package com.teamdev.javaclasses;

/**
 * Throws if sign up fail.
 */
public class SignUpException extends Exception {

    public SignUpException(UserServiceFailCases failCase) {
        super(failCase.getMessage());
    }

    public SignUpException(String message, Throwable cause) {
        super(message, cause);
    }
}
