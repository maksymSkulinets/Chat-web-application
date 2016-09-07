package com.teamdev.javaclasses.repository;

import com.teamdev.javaclasses.entities.ChatId;
import com.teamdev.javaclasses.entities.Chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        if (log.isDebugEnabled()) {
            log.debug("Chat id produce");
        }
        return new ChatId(idCounter.getAndIncrement());
    }
}
