package com.teamdev.javaclasses.repository;

import com.teamdev.javaclasses.DTO.ChatId;
import com.teamdev.javaclasses.entities.Chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class ChatRepository extends InMemoryRepository<Chat, ChatId> {

    private final Logger log = LoggerFactory.getLogger(ChatRepository.class);

    private AtomicLong idCounter = new AtomicLong(0);

    @Override
    ChatId getNextId() {
        if (log.isDebugEnabled()) {
            log.debug("Chat id produce");
        }
        return new ChatId(idCounter.getAndIncrement());
    }
}
