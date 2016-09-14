package com.teamdev.javaclasses.service;


/**
 * Throws if chat creation is fail.
 */
public class ChatCreationException extends Exception {

    public ChatCreationException(ChatServiceFailCases failCase) {
        super(failCase.getMessage());
    }
}
