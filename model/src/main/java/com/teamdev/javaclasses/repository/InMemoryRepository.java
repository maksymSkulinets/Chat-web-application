package com.teamdev.javaclasses.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract in memory repository.
 *
 * @param <TYPE>    raw type for any inheritance entity
 * @param <TYPE_ID> raw type for any inheritance entity id
 */
public abstract class InMemoryRepository<TYPE, TYPE_ID> implements Repository<TYPE, TYPE_ID> {
    private Map<TYPE, TYPE_ID> storageWithKeyType = new HashMap();
    private Map<TYPE_ID, TYPE> storageWithKeyTypeId = new HashMap();

    @Override
    public void create(TYPE type, TYPE_ID type_id) {
        storageWithKeyType.put(type, type_id);
        storageWithKeyTypeId.put(type_id, type);
    }

    @Override
    public TYPE readType(TYPE_ID type_id) {
        return storageWithKeyTypeId.get(type_id);
    }

    @Override
    public TYPE_ID readId(TYPE type) {
        return storageWithKeyType.get(type);
    }
}
