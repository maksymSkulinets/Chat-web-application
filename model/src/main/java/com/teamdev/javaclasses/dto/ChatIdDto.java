package com.teamdev.javaclasses.dto;

public class ChatIdDto {
    private final Long id;

    public ChatIdDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatIdDto chatIdDto = (ChatIdDto) o;

        return id != null ? id.equals(chatIdDto.id) : chatIdDto.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
