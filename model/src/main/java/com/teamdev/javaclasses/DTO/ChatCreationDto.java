package com.teamdev.javaclasses.DTO;

import com.teamdev.javaclasses.entities.UserId;

public class ChatCreationDto {

    private final UserId userId;
    private final String chatName;

    public ChatCreationDto(UserId userId, String chatName) {
        this.userId = userId;
        this.chatName = chatName;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getChatName() {
        return chatName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatCreationDto that = (ChatCreationDto) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return chatName != null ? chatName.equals(that.chatName) : that.chatName == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (chatName != null ? chatName.hashCode() : 0);
        return result;
    }


}
