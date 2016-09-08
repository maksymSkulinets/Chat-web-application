package com.teamdev.javaclasses.service;


/**
 * Throws if chat creation is fail.
 */
public class ChatCreationException extends Exception {
    public ChatCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatCreationException(String message) {
        super(message);
    }
}
