package com.teamdev.javaclasses.entities;

/**
 * Unique token for access to system.
 */
public class AccessToken {
    private final long value;

    public AccessToken(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
