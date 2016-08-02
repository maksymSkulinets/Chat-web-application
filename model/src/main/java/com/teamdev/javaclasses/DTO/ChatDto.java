package com.teamdev.javaclasses.DTO;

import com.teamdev.javaclasses.entities.Message;
import com.teamdev.javaclasses.entities.UserId;

import java.util.List;

/**
 * DTO for transfer chats data.
 */
class ChatDto {
    private final ChatId chatId;
    private final String chatName;
    private final UserId owner;
    private final List<UserId> members;
    private final List<Message> messages;

    public ChatDto(List<Message> messages, List<UserId> members, UserId owner, String chatName, ChatId chatId) {
        this.messages = messages;
        this.members = members;
        this.owner = owner;
        this.chatName = chatName;
        this.chatId = chatId;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public UserId getOwner() {
        return owner;
    }

    public List<UserId> getMembers() {
        return members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatDto chatDto = (ChatDto) o;

        if (chatId != null ? !chatId.equals(chatDto.chatId) : chatDto.chatId != null) return false;
        if (chatName != null ? !chatName.equals(chatDto.chatName) : chatDto.chatName != null) return false;
        if (owner != null ? !owner.equals(chatDto.owner) : chatDto.owner != null) return false;
        if (members != null ? !members.equals(chatDto.members) : chatDto.members != null) return false;
        return messages != null ? messages.equals(chatDto.messages) : chatDto.messages == null;

    }

    @Override
    public int hashCode() {
        int result = chatId != null ? chatId.hashCode() : 0;
        result = 31 * result + (chatName != null ? chatName.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (members != null ? members.hashCode() : 0);
        result = 31 * result + (messages != null ? messages.hashCode() : 0);
        return result;
    }
}
