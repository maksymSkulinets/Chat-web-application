package com.teamdev.javaclasses.DTO;

import com.teamdev.javaclasses.entities.UserId;

/**
 * DTO for post messages to chat.
 */
public class MessageDto {
    private final UserId userId;
    private final ChatId chatId;
    private final String message;

    public MessageDto(UserId userId, ChatId chatId, String message) {
        this.userId = userId;
        this.chatId = chatId;
        this.message = message;
    }

    public UserId getUserId() {
        return userId;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageDto that = (MessageDto) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (chatId != null ? !chatId.equals(that.chatId) : that.chatId != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (chatId != null ? chatId.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
