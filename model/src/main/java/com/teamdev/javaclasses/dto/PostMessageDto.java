package com.teamdev.javaclasses.dto;

/**
 * dto for post messages to chat.
 */
public class PostMessageDto {
    private final Long chatId;
    private final Long userId;
    private final String userName;
    private final String message;

    public PostMessageDto(Long chatId, Long userId, String userName, String message) {
        this.chatId = chatId;
        this.userId = userId;
        this.userName = userName;
        this.message = message;
    }

    public Long getChatId() {
        return chatId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostMessageDto that = (PostMessageDto) o;

        if (chatId != null ? !chatId.equals(that.chatId) : that.chatId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        int result = chatId != null ? chatId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
