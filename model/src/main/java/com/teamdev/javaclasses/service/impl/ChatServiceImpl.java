package com.teamdev.javaclasses.service.impl;

import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.entities.Chat;
import com.teamdev.javaclasses.entities.ChatId;
import com.teamdev.javaclasses.entities.Message;
import com.teamdev.javaclasses.entities.tinyTypes.ChatName;
import com.teamdev.javaclasses.entities.tinyTypes.MessageContent;
import com.teamdev.javaclasses.entities.tinyTypes.UserId;
import com.teamdev.javaclasses.entities.tinyTypes.UserName;
import com.teamdev.javaclasses.repository.impl.ChatRepository;
import com.teamdev.javaclasses.service.ChatCreationException;
import com.teamdev.javaclasses.service.ChatMemberException;
import com.teamdev.javaclasses.service.ChatService;
import com.teamdev.javaclasses.service.PostMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.teamdev.javaclasses.service.ChatServiceFailCases.*;

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
    public ChatIdDto create(ChatCreationDto chatCreationDto) throws ChatCreationException {

        checkNotNull(chatCreationDto.getChatName());
        checkNotNull(chatCreationDto.getUserId());

        final String trimmedChatName = chatCreationDto.getChatName().trim();
        final Long chatOwnerId = chatCreationDto.getUserId();

        if (trimmedChatName.isEmpty()) {
            log.warn(EMPTY_CHAT_NAME.getMessage());
            throw new ChatCreationException(EMPTY_CHAT_NAME);
        }

        final Optional<Chat> chatEntity = chatRepository.getChat(trimmedChatName);

        if (chatEntity.isPresent()) {
            log.warn(NON_UNIQUE_CHAT_NAME.getMessage());
            throw new ChatCreationException(NON_UNIQUE_CHAT_NAME);
        }

        final Chat currentChat = new Chat(new ChatName(trimmedChatName), new UserId(chatOwnerId));

        chatRepository.add(currentChat);

        if (log.isDebugEnabled()) {
            log.debug("Chat name: " + trimmedChatName + " id: " + currentChat.getId().getValue());
        }

        if (log.isInfoEnabled()) {
            log.info("Chat with name: " + trimmedChatName + " successfully created.");
        }

        return new ChatIdDto(currentChat.getId().getValue());
    }

    @Override
    public void joinChat(MemberChatDto memberChatDto) throws ChatMemberException {

        checkNotNull(memberChatDto.getUserId(), "value of user id can not be null");
        checkNotNull(memberChatDto.getChatId(), "value of chat id can not be null");

        final Chat chat = chatRepository.get(new ChatId(memberChatDto.getChatId()));

        final List<UserId> members = chat.getMembers();
        final UserId userId = new UserId(memberChatDto.getUserId());
        if (members.contains(userId)) {
            log.warn(CHAT_MEMBER_ALREADY_JOIN.getMessage());
            throw new ChatMemberException(CHAT_MEMBER_ALREADY_JOIN);
        }

        members.add(new UserId(memberChatDto.getUserId()));

        if (log.isDebugEnabled()) {
            log.debug("Member was added to chat." +
                    " Chat id:" + memberChatDto.getChatId() +
                    " Member id: " + memberChatDto.getUserId());
        }
    }

    @Override
    public void leaveChat(MemberChatDto memberChatDto) throws ChatMemberException {

        checkNotNull(memberChatDto.getUserId());
        checkNotNull(memberChatDto.getChatId());

        final Chat chat = chatRepository.get(new ChatId(memberChatDto.getChatId()));

        final List<UserId> chatMembers = chat.getMembers();
        final UserId userId = new UserId(memberChatDto.getUserId());
        if (!chatMembers.contains(userId)) {
            throw new ChatMemberException(NOT_A_CHAT_MEMBER);
        }

        chat.getMembers().remove(new UserId(memberChatDto.getUserId()));

        if (log.isDebugEnabled()) {
            log.debug("Member was removed from chat." +
                    " Chat id is: " + memberChatDto.getChatId() +
                    " Member id is : " + memberChatDto.getUserId());
        }
    }

    @Override
    public void postMessage(PostMessageDto postMessageDto) throws PostMessageException {

        checkNotNull(postMessageDto.getChatId());
        checkNotNull(postMessageDto.getUserName());
        checkNotNull(postMessageDto.getMessage());

        final Chat chat = chatRepository.get(new ChatId(postMessageDto.getChatId()));

        if (!chat.getMembers().contains(new UserId(postMessageDto.getUserId()))) {
            log.warn(NOT_A_CHAT_MEMBER.getMessage());
            throw new PostMessageException(NOT_A_CHAT_MEMBER);
        }

        if (postMessageDto.getMessage().trim().isEmpty()) {
            log.warn(EMPTY_MESSAGE.getMessage());
            throw new PostMessageException(EMPTY_MESSAGE);
        }

        final UserName userName = new UserName(postMessageDto.getUserName());
        final MessageContent content = new MessageContent(postMessageDto.getMessage());
        chat.getMessages().add(new Message(userName, content));

        if (log.isDebugEnabled()) {
            log.debug("Message was posted successfully." +
                    " To chat with id:" + postMessageDto.getChatId() +
                    " Message author id" + postMessageDto.getUserId());
        }
    }

    @Override
    public void removeChat(ChatIdDto chatIdDto) {

        checkNotNull(chatIdDto.getValue());

        final Chat chat = chatRepository.remove(new ChatId(chatIdDto.getValue()));
        if (log.isDebugEnabled()) {
            log.debug("Chat with id " + chat.getId().getValue() + " was removed.");
        }
    }

    @Override
    public List<UserIdDto> findChatMembers(ChatIdDto chatIdDto) {

        checkNotNull(chatIdDto.getValue());

        final Chat chat = chatRepository.get(new ChatId(chatIdDto.getValue()));
        final List<UserIdDto> membersDto = new ArrayList<>();

        for (UserId current : chat.getMembers()) {
            membersDto.add(new UserIdDto(current.getValue()));
        }

        return membersDto;
    }

    @Override
    public List<MessageDto> findChatMessages(ChatIdDto chatIdDto) {

        checkNotNull(chatIdDto.getValue());

        final Chat chat = chatRepository.get(new ChatId(chatIdDto.getValue()));
        final List<Message> messages = chat.getMessages();
        final List<MessageDto> messagesDto = new ArrayList<>();

        for (Message current : messages) {
            final String ownerNameValue = current.getUserName().getValue();
            final String messageContentValue = current.getContent().getValue();
            messagesDto.add(new MessageDto(ownerNameValue, messageContentValue));
        }

        return messagesDto;
    }

    @Override
    public Optional<ChatDto> findChat(ChatIdDto chatIdDto) {

        checkNotNull(chatIdDto.getValue());

        final Chat chat = chatRepository.get(new ChatId(chatIdDto.getValue()));

        if (chat == null) {
            return Optional.empty();
        }

        final List<MessageDto> messagesDto = new ArrayList<>();
        final List<Message> messages = chat.getMessages();
        for (Message current : messages) {
            messagesDto.add(new MessageDto(current.getUserName().getValue(), current.getContent().getValue()));
        }

        final List<Long> membersDto = new ArrayList<>();
        final List<UserId> members = chat.getMembers();
        for (UserId current : members) {
            membersDto.add(current.getValue());
        }

        final Long chatIdValue = chat.getId().getValue();
        final Long ownerIdValue = chat.getOwnerId().getValue();
        final String chatNameValue = chat.getChatName().getValue();
        final ChatDto chatDto = new ChatDto(chatIdValue, ownerIdValue, chatNameValue, membersDto, messagesDto);
        return Optional.of(chatDto);
    }

    @Override
    public Collection<ChatDto> findAllChats() {

        final Collection<Chat> chats = chatRepository.findAll();
        final Collection<ChatDto> chatsDto = new ArrayList<>();

        for (Chat chat : chats) {
            final List<Message> messages = chat.getMessages();
            final List<UserId> members = chat.getMembers();
            List<MessageDto> messagesDto = new ArrayList<>();
            List<Long> membersDto = new ArrayList<>();

            for (Message current : messages) {
                final String authorName = current.getUserName().getValue();
                final String content = current.getContent().getValue();
                messagesDto.add(new MessageDto(authorName, content));
            }

            for (UserId current : members) {
                membersDto.add(current.getValue());
            }

            final long chatId = chat.getId().getValue();
            final long ownerId = chat.getOwnerId().getValue();
            final String chatName = chat.getChatName().getValue();
            chatsDto.add(new ChatDto(chatId, ownerId, chatName, membersDto, messagesDto));
        }

        return chatsDto;
    }

    @Override
    public Optional<ChatIdDto> findChatIdByName(ChatNameDto chatName) {
        final Optional<Chat> chat = chatRepository.getChat(chatName.getValue());
        if (chat.isPresent()) {
            final long chatIdValue = chat.get().getId().getValue();
            return Optional.of(new ChatIdDto(chatIdValue));
        }
        return Optional.empty();
    }

}
