package com.teamdev.javaclasses.entities;

/**
 * Sending message data.
 */
public class Message {
    private final String authorName;
    private final String content;

    public Message(String authorName, String content) {
        this.authorName = authorName;
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (authorName != null ? !authorName.equals(message.authorName) : message.authorName != null) return false;
        return content != null ? content.equals(message.content) : message.content == null;

    }

    @Override
    public int hashCode() {
        int result = authorName != null ? authorName.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
