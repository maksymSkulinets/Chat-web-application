package com.teamdev.javaclasses;

/**
 * User login wrong scenarios.
 */
public enum UserServiceFailCases {

    PASSWORDS_NOT_MATCH("Passwords must match"),
    EXIST_USER("Current nickname must be unique"),
    NON_SIGN_UP_USER("Such user must register before"),
    EMPTY_INPUT("All fields must be filled");

    private String message;

    UserServiceFailCases(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
