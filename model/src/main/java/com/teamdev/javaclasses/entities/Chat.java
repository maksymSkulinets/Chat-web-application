package com.teamdev.javaclasses.entities;

import com.teamdev.javaclasses.entities.tinyTypes.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Chat entity
 */
public class Chat implements Entity {
    private final String chatName;
    private final UserId owner;
    private ChatId chatId;
    private List<UserId> members = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public Chat(String chatName, UserId owner) {
        this.chatName = chatName;
        this.owner = owner;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public UserId getOwnerId() {
        return owner;
    }

    public List<UserId> getMembers() {
        return members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public EntityId getId() {
        return chatId;
    }

    @Override
    public void setId(EntityId id) {
        chatId = new ChatId(id.getValue());
    }
}
