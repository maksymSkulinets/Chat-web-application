package com.teamdev.javaclasses.entities;

public interface Repository<TYPE, TYPE_ID> {
    void create(TYPE type, TYPE_ID type_id);

    TYPE read(TYPE_ID type_id);
}
