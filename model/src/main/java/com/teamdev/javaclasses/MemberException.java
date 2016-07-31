package com.teamdev.javaclasses;

public class MemberException extends Exception {
    public MemberException(String message) {
        super(message);
    }

    public MemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
