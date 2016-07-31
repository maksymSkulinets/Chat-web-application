package com.teamdev.javaclasses.DTO;

import com.teamdev.javaclasses.entities.UserId;

/**
 * DTO for post messages to chat.
 */
public class MessageDto {
    private final UserId userId;
    private final ChatId chatId;
    private final String message;
    private final String nickName;

    public MessageDto(UserId userId, ChatId chatId, String message, String nickName) {
        this.userId = userId;
        this.chatId = chatId;
        this.message = message;
        this.nickName = nickName;
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

    public String getNickName() {
        return nickName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageDto that = (MessageDto) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (chatId != null ? !chatId.equals(that.chatId) : that.chatId != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return nickName != null ? nickName.equals(that.nickName) : that.nickName == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (chatId != null ? chatId.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (nickName != null ? nickName.hashCode() : 0);
        return result;
    }
}
