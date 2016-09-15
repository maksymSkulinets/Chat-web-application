package com.teamdev.javaclasses.service;

/**
 * А
 */
public class SignUpException extends Exception {
    public SignUpException(UserServiceFailCases failCase) {
        super(failCase.getMessage());
    }
}
