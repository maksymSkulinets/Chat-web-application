import com.teamdev.javaclasses.*;
import com.teamdev.javaclasses.DTO.*;
import com.teamdev.javaclasses.entities.Chat;
import com.teamdev.javaclasses.entities.Message;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.impl.ChatServiceImpl;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import org.junit.Test;

import java.util.List;

import static com.teamdev.javaclasses.ChatServiceFailCases.*;
import static org.junit.Assert.*;

public class ChatServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    private UserDTO successfulSignUp(SignUpDTO signUpData) {
        UserDTO result = null;
        try {
            result = userService.signUp(signUpData);
        } catch (SignUpException e) {
            fail("Sign up with valid data is not successful");
        }
        return result;
    }

    private SecurityTokenDTO successfulLogin(LoginDTO loginData) {
        SecurityTokenDTO result = null;
        try {
            result = userService.login(loginData);
        } catch (LoginException e) {
            fail("Sign up with valid data is not successful");
        }
        return result;
    }

    private ChatId successfulChatCreation(ChatCreationDto chatCreationDto) {
        ChatId result = null;
        try {
            result = chatService.createChat(chatCreationDto);
        } catch (ChatCreationException e) {
            fail("Chat creation with valid data is not successful");
        }
        return result;
    }


    @Test
    public void addNewChat() {
        final String nickName = "Mike";
        final String password = "mike_password";
        final String chatName = "box";

        final UserDTO userDTO = successfulSignUp(new SignUpDTO(nickName, password, password));
        successfulLogin(new LoginDTO(nickName, password));
        ChatId actualChatId = null;

        try {
            final ChatCreationDto chatCreationDto = new ChatCreationDto(new UserId((userDTO.getUserId())), chatName);
            actualChatId = chatService.createChat(chatCreationDto);
        } catch (ChatCreationException e) {
            assertNotNull("New chat was not created.");
        }

        assertNotNull("Chat id is null.", actualChatId);

        final Chat chat = chatService.getChat(actualChatId);
        assertEquals("Chat owner ids is not equals",
                chat.getOwnerId().getValue(), userDTO.getUserId());
        assertEquals("Chat ids is not equals",
                chat.getId().getValue(), actualChatId.getValue());

    }

    @Test
    public void failAdditionNewChatWithEmptyInput() {

        final String nickName = "Harry";
        final String password = "Harry_password";
        final String chatName = "      ";

        final UserDTO userDTO = successfulSignUp(new SignUpDTO(nickName, password, password));
        successfulLogin(new LoginDTO(nickName, password));

        try {
            chatService.createChat(new ChatCreationDto(new UserId((userDTO.getUserId())), chatName));
            fail("Chat creation exception was not thrown");
        } catch (ChatCreationException e) {
            assertEquals("Chat creation fail message are not match",
                    e.getMessage(), EMPTY_CHAT_NAME.getMessage());
        }
    }

    @Test
    public void failAdditionNewChatWithAlreadyCreatedChatName() throws ChatCreationException {
        final String nickName = "Elizabet_Queen";
        final String password = "GOD_SAVE_A_KINGDOM!";
        final String chatName = "i_am_a_QUEEN!!!GOT YOU KNEES!!";

        final UserDTO userDTO = successfulSignUp(new SignUpDTO(nickName, password, password));
        successfulLogin(new LoginDTO(nickName, password));
        successfulChatCreation(new ChatCreationDto(new UserId(userDTO.getUserId()), chatName));

        try {
            chatService.createChat(new ChatCreationDto(new UserId((userDTO.getUserId())), chatName));
            fail("Chat creation exception was not thrown");
        } catch (ChatCreationException e) {
            assertEquals("Chat creation fail message are not match",
                    e.getMessage(), NON_UNIQUE_CHAT_NAME.getMessage());
        }
    }

    @Test
    public void failAddDuplicateMemberToChat() throws MemberException {
        final String nickName = "Homer_Simpson";
        final String password = "homer_password";
        final String chatName = "beer";

        final UserDTO userDTO = successfulSignUp(new SignUpDTO(nickName, password, password));
        final SecurityTokenDTO securityTokenDTO = successfulLogin(new LoginDTO(nickName, password));
        final ChatId chatIdDTO = successfulChatCreation(new ChatCreationDto(new UserId(userDTO.getUserId()), chatName));

        chatService.addMember(new MemberChatDto(new UserId(userDTO.getUserId()), chatIdDTO));

        try {
            chatService.addMember(new MemberChatDto(new UserId(userDTO.getUserId()), chatIdDTO));
            fail("Member exception was not thrown");
        } catch (MemberException e) {
            assertEquals("Add chat member fail message are not match",
                    CHAT_MEMBER_ALREADY_JOIN.getMessage(), e.getMessage());
        }
    }

    @Test
    public void addMemberToChat() {
        final String nickName = "Jimmy_Hendrix";
        final String password = "jimmy_password";
        final String chatName = "guitars, nothing else";

        final UserDTO userDTO = successfulSignUp(new SignUpDTO(nickName, password, password));
        successfulLogin(new LoginDTO(nickName, password));
        final ChatId chatIdDTO = successfulChatCreation(new ChatCreationDto(new UserId(userDTO.getUserId()), chatName));

        try {
            chatService.addMember(new MemberChatDto(new UserId(userDTO.getUserId()), chatIdDTO));
        } catch (MemberException e) {
            fail("Add member failed");
        }

        final List<UserId> members = chatService.getChat(chatIdDTO).getMembers();

        if (!members.contains(new UserId(userDTO.getUserId()))) {
            fail("User was not set like member of current chat.");
        }

    }

    @Test
    public void removeChatMember() {
        final String nickName = "Robert_Johnson";
        final String password = "robert_password";
        final String chatName = "blues";

        final UserDTO userDTO = successfulSignUp(new SignUpDTO(nickName, password, password));
        successfulLogin(new LoginDTO(nickName, password));
        final ChatId chatIdDTO = successfulChatCreation(new ChatCreationDto(new UserId(userDTO.getUserId()), chatName));

        try {
            chatService.addMember(new MemberChatDto(new UserId(userDTO.getUserId()), chatIdDTO));
        } catch (MemberException e) {
            fail("Add member failed");
        }

        List<UserId> chatMembers = chatService.getChat(chatIdDTO).getMembers();

        if (!chatMembers.contains(new UserId(userDTO.getUserId()))) {
            fail("User is not in membership of current chat.");
        }

        try {
            chatService.removeMember((new MemberChatDto(new UserId(userDTO.getUserId()), chatIdDTO)));
        } catch (MemberException e) {
            fail("Remove member failed");
        }

        chatMembers = chatService.getChat(chatIdDTO).getMembers();

        if (chatMembers.contains(new UserId(userDTO.getUserId()))) {
            fail("User is in membership of current chat but was removed.");
        }
    }

    @Test
    public void removeChatMemberFailNotAChatMember() {
        final String nickName = "Arnold_Scwarseneger";
        final String password = "arnold_password";
        final String chatName = "body_building";

        final UserDTO userDTO = successfulSignUp(new SignUpDTO(nickName, password, password));
        successfulLogin(new LoginDTO(nickName, password));
        final ChatId chatIdDTO = successfulChatCreation(new ChatCreationDto(new UserId(userDTO.getUserId()), chatName));

        try {
            chatService.removeMember((new MemberChatDto(new UserId(userDTO.getUserId()), chatIdDTO)));
        } catch (MemberException e) {
            assertEquals("Add chat member fail message are not match", NOT_A_CHAT_MEMBER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void postMessageToChat() {

        final String nickName = "Joe_Satriani";
        final String password = "joe_password";
        final String chatName = "G3";
        final String messageContent = "My first message!";

        final UserDTO userDTO = successfulSignUp(new SignUpDTO(nickName, password, password));
        successfulLogin(new LoginDTO(nickName, password));
        final ChatId chatIdDTO = successfulChatCreation(new ChatCreationDto(new UserId(userDTO.getUserId()), chatName));

        try {
            chatService.addMember(new MemberChatDto(new UserId(userDTO.getUserId()), chatIdDTO));
        } catch (MemberException e) {
            fail("Add member failed");
        }

        try {
            chatService.sendMessage(new MessageDTO(new UserId(userDTO.getUserId()), chatIdDTO, messageContent, userDTO.getNickname()));
        } catch (MessageException e) {
            fail("Sending message fail.");
        }

        final List<Message> messages = chatService.getChat(chatIdDTO).getMessages();
        final Message actualMessage = new Message(userDTO.getNickname(), messageContent);

        if (!messages.contains(actualMessage)) {
            fail("Message was not posted.");
        }
    }

    @Test
    public void sendMessageToChatFailNotAChatMember() {
        final String nickName = "Usain_Bolt";
        final String password = "bolt_password";
        final String chatName = "running";
        final String messageContent = "My second message!";

        final UserDTO userDTO = successfulSignUp(new SignUpDTO(nickName, password, password));
        successfulLogin(new LoginDTO(nickName, password));
        final ChatId chatIdDTO = successfulChatCreation(new ChatCreationDto(new UserId(userDTO.getUserId()), chatName));

        try {
            chatService.sendMessage(new MessageDTO(new UserId(userDTO.getUserId()), chatIdDTO, messageContent, userDTO.getNickname()));
        } catch (MessageException e) {
            assertEquals("Add chat member fail message are not match", NOT_A_CHAT_MEMBER.getMessage(), e.getMessage());
        }

    }
}
