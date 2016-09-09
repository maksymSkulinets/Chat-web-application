package com.teamdev.javaclasses.dto;

/**
 * dto for join and un join chats.
 */
public class MemberChatDto {
    private final Long userId;
    private final Long chatId;

    public MemberChatDto(Long userId, Long chatId) {
        this.userId = userId;
        this.chatId = chatId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getChatId() {
        return chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberChatDto that = (MemberChatDto) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return chatId != null ? chatId.equals(that.chatId) : that.chatId == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (chatId != null ? chatId.hashCode() : 0);
        return result;
    }
}
