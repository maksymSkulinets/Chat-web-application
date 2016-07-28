package com.teamdev.javaclasses;

/**
 * User wrong sign up scenarios.
 */
public enum SignUpFailCases {

    EMPTY_INPUT("All fields must be filled"),
    PASSWORDS_NOT_MATCH("Passwords must match"),
    EXIST_USER("Current nickname must be unique");

    private String message;

    SignUpFailCases(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
