package com.teamdev.javaclasses.dto;

public class ChatIdDto {
    private long id;

    public ChatIdDto(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatIdDto chatIdDto = (ChatIdDto) o;

        return id == chatIdDto.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
