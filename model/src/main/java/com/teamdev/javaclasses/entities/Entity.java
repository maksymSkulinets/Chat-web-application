package com.teamdev.javaclasses.entities;

/**
 * Abstract entity interface.
 *
 * @param <TypeId> type of entity id
 */
public interface Entity<TypeId extends EntityId> {

    TypeId getValue();

    void setId(TypeId id);
}
