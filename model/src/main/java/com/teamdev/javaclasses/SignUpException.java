package com.teamdev.javaclasses;

import com.teamdev.javaclasses.service.UserServiceFailCases;

/**
 * А
 */
public class SignUpException extends Exception {

    public SignUpException(UserServiceFailCases failCase) {
        super(failCase.getMessage());
    }

    public SignUpException(String message, Throwable cause) {
        super(message, cause);
    }
}
