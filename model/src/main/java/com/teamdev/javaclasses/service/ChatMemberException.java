package com.teamdev.javaclasses.service;

public class ChatMemberException extends Exception {

    public ChatMemberException(ChatServiceFailCases failCase) {
        super(failCase.getMessage());
    }
}
