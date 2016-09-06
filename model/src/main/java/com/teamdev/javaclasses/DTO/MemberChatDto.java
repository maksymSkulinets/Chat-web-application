package com.teamdev.javaclasses.dto;

import com.teamdev.javaclasses.entities.ChatId;
import com.teamdev.javaclasses.entities.UserId;

/**
 * dto for join and un join chats.
 */
public class MemberChatDto {
    /*TODO simplify MemberChatDto structure with raw types*/
    private final UserId userId;
    private final ChatId chatId;

    public MemberChatDto(UserId userId, ChatId chatId) {
        this.userId = userId;
        this.chatId = chatId;
    }

    public UserId getUserId() {
        return userId;
    }

    public ChatId getChatId() {
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
