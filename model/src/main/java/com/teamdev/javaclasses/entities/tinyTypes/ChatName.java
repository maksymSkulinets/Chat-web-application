package com.teamdev.javaclasses.entities.tinyTypes;

public class ChatName {
    private final String value;

    public ChatName(String chatName) {
        this.value = chatName;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatName chatName1 = (ChatName) o;

        return value != null ? value.equals(chatName1.value) : chatName1.value == null;

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
