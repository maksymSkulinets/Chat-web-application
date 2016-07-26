package com.teamdev.javaclasses.entities;

/**
 * Abstract entity.
 */
public abstract class EntityId {
    private final long value;

    public EntityId(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
