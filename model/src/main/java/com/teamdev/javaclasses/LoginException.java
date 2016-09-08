package com.teamdev.javaclasses;

import com.teamdev.javaclasses.service.UserServiceFailCases;

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
