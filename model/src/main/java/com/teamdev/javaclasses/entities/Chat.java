package com.teamdev.javaclasses.entities;

import com.teamdev.javaclasses.entities.tinyTypes.ChatName;
import com.teamdev.javaclasses.entities.tinyTypes.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Chat entity
 */
public class Chat implements Entity {
    private ChatName chatName;
    private UserId ownerId;
    private ChatId chatId;
    private List<UserId> members = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public Chat(ChatName name, UserId ownerId) {
        this.chatName = name;
        this.ownerId = ownerId;
    }

    public ChatName getChatName() {
        return chatName;
    }

    public UserId getOwnerId() {
        return ownerId;
    }

    public List<UserId> getMembers() {
        return members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public EntityId getId() {
        return chatId;
    }

    @Override
    public void setId(EntityId id) {
        chatId = new ChatId(id.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        if (chatName != null ? !chatName.equals(chat.chatName) : chat.chatName != null) return false;
        if (ownerId != null ? !ownerId.equals(chat.ownerId) : chat.ownerId != null) return false;
        if (chatId != null ? !chatId.equals(chat.chatId) : chat.chatId != null) return false;
        if (members != null ? !members.equals(chat.members) : chat.members != null) return false;
        return messages != null ? messages.equals(chat.messages) : chat.messages == null;

    }

    @Override
    public int hashCode() {
        int result = chatName != null ? chatName.hashCode() : 0;
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + (chatId != null ? chatId.hashCode() : 0);
        result = 31 * result + (members != null ? members.hashCode() : 0);
        result = 31 * result + (messages != null ? messages.hashCode() : 0);
        return result;
    }
}
