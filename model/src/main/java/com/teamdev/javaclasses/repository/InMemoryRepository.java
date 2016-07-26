package com.teamdev.javaclasses.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract in memory repository
 *
 * @param <TYPE>
 * @param <TYPE_ID>
 */
public abstract class InMemoryRepository<TYPE, TYPE_ID> implements Repository<TYPE, TYPE_ID> {
    private Map<TYPE, TYPE_ID> storageKey = new HashMap();
    private Map<TYPE_ID, TYPE> storageValue = new HashMap();
    @Override
    public void create(TYPE type, TYPE_ID type_id) {
        storageKey.put(type, type_id);
        storageValue.put(type_id, type);
    }
    @Override
    public TYPE read(TYPE_ID type_id) {
        return storageValue.get(type_id);
    }
}
