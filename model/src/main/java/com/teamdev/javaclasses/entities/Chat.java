package com.teamdev.javaclasses.entities;

import com.teamdev.javaclasses.DTO.ChatId;

import java.util.List;

/**
 * Chat entity
 */
public class Chat implements Entity {
    private final String chatName;
    private final UserId owner;
    private ChatId chatId;
    private List<UserId> members;
    private List<Message> messages;

    public Chat(String chatName, UserId owner) {
        this.chatName = chatName;
        this.owner = owner;
    }

    public Chat(ChatId chatId, String chatName, UserId owner, List<UserId> members, List<Message> messages) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.owner = owner;
        this.members = members;
        this.messages = messages;
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
