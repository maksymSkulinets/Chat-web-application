package com.teamdev.javaclasses.service;

/**
 * Chat wrong scenarios
 */
public enum ChatServiceFailCases {

    EMPTY_CHAT_NAME("Chat name must be filled."),
    NON_UNIQUE_CHAT_NAME("Chat name must be unique."),
    CHAT_MEMBER_ALREADY_JOIN("Already joined."),
    NOT_A_CHAT_MEMBER("User is not connect to the current chat.");

    private String message;

    ChatServiceFailCases(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    }
