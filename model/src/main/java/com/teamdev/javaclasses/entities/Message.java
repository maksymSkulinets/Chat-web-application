package com.teamdev.javaclasses.entities;

import com.teamdev.javaclasses.entities.tinyTypes.MessageContent;
import com.teamdev.javaclasses.entities.tinyTypes.UserName;

/**
 * Sending message data.
 */
public class Message {
    private final UserName userName;
    private final MessageContent content;

    public Message(UserName userName, MessageContent content) {
        this.userName = userName;
        this.content = content;
    }

    public UserName getUserName() {
        return userName;
    }

    public MessageContent getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (userName != null ? !userName.equals(message.userName) : message.userName != null) return false;
        return content != null ? content.equals(message.content) : message.content == null;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
