package com.teamdev.javaclasses;

/**
 * Chat wrong scenarios
 */
public enum ChatFailCases {

    EMPTY_CHAT_NAME("Chat name must be filled."),
    NON_UNIQUE_CHAT_NAME("Chat name must be unique."),
    CHAT_MEMBER_ALREADY_JOIN("Already connected."),
    NOT_A_CHAT_MEMBER("User not connect to current chat.");

    private String message;

    ChatFailCases(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    }
