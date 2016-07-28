package com.teamdev.javaclasses;

/**
 * User wrong login scenarios.
 */
public enum LoginFailCases {

    NON_SIGN_UP_USER("Such user must register before"),
    EMPTY_INPUT("All fields must be filled");

    private String message;

    LoginFailCases(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
