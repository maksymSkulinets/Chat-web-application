package com.teamdev.javaclasses.repository;

public interface Repository<TYPE, TYPE_ID> {
    void create(TYPE type, TYPE_ID type_id);

    TYPE readType(TYPE_ID type_id);

    TYPE_ID readId(TYPE type);
}
