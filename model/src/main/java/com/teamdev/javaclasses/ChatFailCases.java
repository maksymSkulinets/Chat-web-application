package com.teamdev.javaclasses;

/**
 * Chat wrong scenarios
 */
public enum ChatFailCases {
    SOME("message");

    private String message;

    ChatFailCases(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
