package com.teamdev.javaclasses.repository;

import com.teamdev.javaclasses.entities.Entity;
import com.teamdev.javaclasses.entities.EntityId;

import java.util.Collection;

/**
 * Public API of repository
 *
 * @param <TYPE>    raw type for any inheritance entity
 * @param <TYPE_ID> raw type for any inheritance entity id
 */
interface Repository<TYPE extends Entity, TYPE_ID extends EntityId> {
    void add(TYPE type);

    TYPE find(TYPE_ID type_id);

    Collection<TYPE> findAll();

}
