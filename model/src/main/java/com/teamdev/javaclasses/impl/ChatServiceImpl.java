package com.teamdev.javaclasses.impl;

import com.teamdev.javaclasses.*;
import com.teamdev.javaclasses.DTO.ChatCreationDto;
import com.teamdev.javaclasses.DTO.ChatId;
import com.teamdev.javaclasses.DTO.MemberChatDto;
import com.teamdev.javaclasses.DTO.MessageDTO;
import com.teamdev.javaclasses.entities.Chat;
import com.teamdev.javaclasses.entities.Message;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.repository.ChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.teamdev.javaclasses.ChatServiceFailCases.CHAT_MEMBER_ALREADY_JOIN;
import static com.teamdev.javaclasses.ChatServiceFailCases.NOT_A_CHAT_MEMBER;

/**
 * Implementation of {@link ChatService}.
 */
public class ChatServiceImpl implements ChatService {
    private static ChatService chatService;
    private final ChatRepository chatRepository = ChatRepository.getInstance();
    private final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    private ChatServiceImpl() {
    }

    public static ChatService getInstance() {
        if (chatService == null) {
            chatService = new ChatServiceImpl();
        }
        return chatService;
    }

    @Override
    public ChatId createChat(ChatCreationDto chatCreationDto) throws ChatCreationException {

        String trimmedChatName = chatCreationDto.getChatName().trim();

        if (trimmedChatName.isEmpty()) {
            log.warn("Fail chat creation: chat name input is empty");
            throw new ChatCreationException(ChatServiceFailCases.EMPTY_CHAT_NAME.getMessage());
        }

        for (Chat current : chatRepository.findAll()) {

            if (current.getChatName().equals(trimmedChatName)) {
                log.warn("Fail chat creation: " + trimmedChatName + " - chat name is exist.");
                throw new ChatCreationException(ChatServiceFailCases.NON_UNIQUE_CHAT_NAME.getMessage());
            }

        }

        chatRepository.add(new Chat(trimmedChatName, chatCreationDto.getUserId()));

        ChatId newChatId = null;
        for (Chat current : chatRepository.findAll()) {

            if (current.getChatName().equals(trimmedChatName)) {
                newChatId = current.getChatId();
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Chat was created. Chat name: " + trimmedChatName +
                    " Chat id: " + newChatId.getValue());
        }

        return newChatId;
    }

    @Override
    public void addMember(MemberChatDto memberChatDto) throws MemberException {
        final Chat chat = chatRepository.find(memberChatDto.getChatId());

        for (UserId memberId : chat.getMembers()) {
            if (memberChatDto.getUserId().equals(memberId)) {
                log.warn("Add chat member fail: chat member already join.");
                throw new MemberException(CHAT_MEMBER_ALREADY_JOIN.getMessage());
            }
        }

        chat.getMembers().add(memberChatDto.getUserId());

        if (log.isDebugEnabled()) {
            log.debug("Chat member was added." +
                    " Chat id:" + memberChatDto.getChatId().getValue() +
                    " Member id: " + memberChatDto.getUserId().getValue());
        }

    }

    @Override
    public void removeMember(MemberChatDto memberChatDto) throws MemberException {
        final Chat chat = chatRepository.find(memberChatDto.getChatId());

        final List<UserId> chatMembers = chat.getMembers();

        if (!chatMembers.contains(memberChatDto.getUserId())) {
            log.warn("Remove chat member fail: user not a chat member.");
            throw new MemberException(NOT_A_CHAT_MEMBER.getMessage());
        } else {
            chatMembers.remove(memberChatDto.getUserId());

            if (log.isDebugEnabled()) {
                log.debug("Chat member was removed." +
                        " Chat id is: " + memberChatDto.getChatId().getValue() +
                        " Member id is : " + memberChatDto.getUserId().getValue());
            }

        }
    }

    @Override
    public void sendMessage(MessageDTO messageDto) throws MessageException {
        final Chat chat = chatRepository.find(messageDto.getChatId());

        final List<UserId> chatMembers = chat.getMembers();

        if (!chatMembers.contains(messageDto.getUserId())) {
            log.warn("Post message chat member fail: user not a chat member.");
            throw new MessageException(NOT_A_CHAT_MEMBER.getMessage());
        } else {
            chat.getMessages().add(new Message(messageDto.getNickName(), messageDto.getMessage()));

            if (log.isDebugEnabled()) {
                log.debug("Message was sent successfully." +
                        " To chat with id:" + messageDto.getChatId().getValue() +
                        " Message author:  " + messageDto.getNickName());
            }
        }


    }

    @Override
    public Chat getChat(ChatId id) {
        return chatRepository.find(id);
    }

}
