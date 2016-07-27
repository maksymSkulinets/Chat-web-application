package com.teamdev.javaclasses.entities;

/**
 * Unique token for access to system.
 */
public class AccessToken {
    private final long value;

    public AccessToken() {
        value = System.nanoTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccessToken that = (AccessToken) o;

        return value == that.value;

    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    public long getValue() {
        return value;
    }
}
