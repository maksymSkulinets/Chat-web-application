package com.teamdev.javaclasses.dto;

public class ChatCreationDto {
    private final String chatName;
    private final Long userId;

    public ChatCreationDto(String chatName, Long userId) {
        this.chatName = chatName;
        this.userId = userId;
    }

    public String getChatName() {
        return chatName;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatCreationDto that = (ChatCreationDto) o;

        if (chatName != null ? !chatName.equals(that.chatName) : that.chatName != null) return false;
        return userId != null ? userId.equals(that.userId) : that.userId == null;

    }

    @Override
    public int hashCode() {
        int result = chatName != null ? chatName.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
