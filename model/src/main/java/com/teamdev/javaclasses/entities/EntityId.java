package com.teamdev.javaclasses.entities;

/**
 * Type for entity unique identifier.
 */
public abstract class EntityId {

    private long id;

    public EntityId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityId entityId = (EntityId) o;
        return id == entityId.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public long getValue() {
        return id;
    }
}
