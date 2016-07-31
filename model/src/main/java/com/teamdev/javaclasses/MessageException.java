package com.teamdev.javaclasses;

/**
 * Throws if message can be posted.
 */
public class MessageException extends Exception {
    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
