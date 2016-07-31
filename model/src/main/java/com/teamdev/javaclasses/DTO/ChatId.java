package com.teamdev.javaclasses.DTO;

import com.teamdev.javaclasses.entities.EntityId;

/**
 * Data transfer object for chat id.
 */
public class ChatId extends EntityId {
    private final long id;

    public ChatId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatId chatIdDTO = (ChatId) o;

        return id == chatIdDTO.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
