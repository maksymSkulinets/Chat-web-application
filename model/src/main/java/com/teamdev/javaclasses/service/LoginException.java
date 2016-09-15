package com.teamdev.javaclasses.service;

/**
 * Throws if login fail.
 */
public class LoginException extends Exception {
    public LoginException(UserServiceFailCases failCase) {
        super(failCase.getMessage());
    }
}
