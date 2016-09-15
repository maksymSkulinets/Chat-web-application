package com.teamdev.javaclasses.service;

/**
 * Throws if message can be posted.
 */
public class PostMessageException extends Exception {
    public PostMessageException(ChatServiceFailCases failCase) {
        super(failCase.getMessage());
    }
}
