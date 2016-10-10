package com.teamdev.javaclasses.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * dto for transfer chats data.
 */
public class ChatDto {
    private final Long chatId;
    private final Long ownerId;
    private final String chatName;
    private List<Long> members = new ArrayList<>();
    private List<MessageDto> messages = new ArrayList<>();

    public ChatDto(Long chatId, Long ownerId, String chatName,
                   List<Long> members, List<MessageDto> messages) {
        this.chatId = chatId;
        this.ownerId = ownerId;
        this.chatName = chatName;
        this.members = members;
        this.messages = messages;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public List<Long> getMembers() {
        return members;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatDto chatDto = (ChatDto) o;

        if (chatId != null ? !chatId.equals(chatDto.chatId) : chatDto.chatId != null) return false;
        if (chatName != null ? !chatName.equals(chatDto.chatName) : chatDto.chatName != null) return false;
        if (ownerId != null ? !ownerId.equals(chatDto.ownerId) : chatDto.ownerId != null) return false;
        if (members != null ? !members.equals(chatDto.members) : chatDto.members != null) return false;
        return messages != null ? messages.equals(chatDto.messages) : chatDto.messages == null;

    }

    @Override
    public int hashCode() {
        int result = chatId != null ? chatId.hashCode() : 0;
        result = 31 * result + (chatName != null ? chatName.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + (members != null ? members.hashCode() : 0);
        result = 31 * result + (messages != null ? messages.hashCode() : 0);
        return result;
    }
}
