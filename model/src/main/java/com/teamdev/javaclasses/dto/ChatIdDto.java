package com.teamdev.javaclasses.dto;

public class ChatIdDto {
    private final Long value;

    public ChatIdDto(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatIdDto chatIdDto = (ChatIdDto) o;

        return value != null ? value.equals(chatIdDto.value) : chatIdDto.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
