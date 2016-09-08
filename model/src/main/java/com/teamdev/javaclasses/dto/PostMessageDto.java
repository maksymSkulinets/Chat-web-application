package com.teamdev.javaclasses.dto;

/**
 * dto for post messages to chat.
 */
public class PostMessageDto {
    private final Long userId;
    private final Long chatId;
    private final String message;
    private final String nickName;

    public PostMessageDto(Long userId, Long chatId, String message, String nickName) {
        this.userId = userId;
        this.chatId = chatId;
        this.message = message;
        this.nickName = nickName;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getChatId() {
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

        PostMessageDto that = (PostMessageDto) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (chatId != null ? !chatId.equals(that.chatId) : that.chatId != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (nickName != null ? !nickName.equals(that.nickName) : that.nickName != null) return false;

        return true;
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
