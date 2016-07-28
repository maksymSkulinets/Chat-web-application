package com.teamdev.javaclasses.repository;

import com.teamdev.javaclasses.entities.Entity;
import com.teamdev.javaclasses.entities.EntityId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract in memory repository.
 *
 * @param <TYPE>    raw type for any inheritance entity
 * @param <TYPE_ID> raw type for any inheritance entity id
 */
abstract class InMemoryRepository<TYPE extends Entity, TYPE_ID extends EntityId> implements Repository<TYPE, TYPE_ID> {
    private Map<TYPE_ID, TYPE> storage = new HashMap<>();

    @Override
    public void add(TYPE type) {
        final TYPE_ID id = getNextId();
        type.setId(id);
        storage.put(id, type);
    }

    @Override
    public TYPE find(TYPE_ID type_id) {
        return storage.get(type_id);
    }

    @Override
    public Collection<TYPE> findAll() {
        return storage.values();
    }

    abstract TYPE_ID getNextId();
}
