package com.teamdev.javaclasses.service;

public class ChatMemberException extends Exception {
    public ChatMemberException(String message) {
        super(message);
    }

    public ChatMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
