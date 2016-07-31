package com.teamdev.javaclasses;

/**
 * Chat wrong scenarios
 */
public enum ChatFailCases {

    EMPTY_CHAT_NAME("Chat name must be filled."),
    NON_UNIQUE_CHAT_NAME("Chat name must be unique.");

    private String message;

    ChatFailCases(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
