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

    @Override
    public boolean equals(Object o) {


        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityId entityId = (EntityId) o;

        return value == entityId.value;

    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }
}
