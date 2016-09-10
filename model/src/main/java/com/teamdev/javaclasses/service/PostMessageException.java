package com.teamdev.javaclasses.service;

/**
 * Throws if message can be posted.
 */
public class PostMessageException extends Exception {
    public PostMessageException(String message) {
        super(message);
    }

    public PostMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
