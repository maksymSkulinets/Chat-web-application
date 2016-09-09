package com.teamdev.javaclasses.repository.impl;

import com.teamdev.javaclasses.entities.Chat;
import com.teamdev.javaclasses.entities.ChatId;
import com.teamdev.javaclasses.repository.InMemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ChatRepository extends InMemoryRepository<Chat, ChatId> {

    private static ChatRepository chatRepository = ChatRepository.getInstance();
    private final Logger log = LoggerFactory.getLogger(ChatRepository.class);
    private final AtomicLong idCounter = new AtomicLong(0);

    private ChatRepository() {
    }

    public static ChatRepository getInstance() {
        if (chatRepository == null) {
            chatRepository = new ChatRepository();
        }
        return chatRepository;
    }

    @Override
    public ChatId getNextId() {
        return new ChatId(idCounter.getAndIncrement());
    }

    public Optional<Chat> getChat(String chatName) {
        final Collection<Chat> allChats = findAll();
        Optional<Chat> result = Optional.empty();

        for (Chat current : allChats) {
            if (current.getChatName().getValue().equals(chatName)) {
                result = Optional.of(current);
                break;
            }
        }
        return result;
    }
}
