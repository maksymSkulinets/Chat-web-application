package com.teamdev.javaclasses.dto;

import com.teamdev.javaclasses.entities.tinyTypes.UserId;

public class ChatCreationDto {

    private final String chatName;
    /*TODO simplify user id var with raw type*/
    private final UserId userId;

    public ChatCreationDto(String chatName, UserId userId) {
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
