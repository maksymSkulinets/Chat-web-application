import com.teamdev.javaclasses.*;
import com.teamdev.javaclasses.DTO.*;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.impl.ChatServiceImpl;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.teamdev.javaclasses.ChatFailCases.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ChatServiceShould {
    private final String nickName = "John";
    private final String password = "password_321123";
    private final String chatName = "technologies";

    private final UserServiceImpl userService = new UserServiceImpl();
    private final ChatService chatService = new ChatServiceImpl();

    private UserDTO userDTO;
    private SecurityTokenDTO securityDTO;

    @Before
    public void setUp() {
        userDTO = successfulSignUp(new SignUpDTO(nickName, password, password));
        securityDTO = successfulLogin(new LoginDTO(nickName, password));
    }

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


    @Test
    public void addNewChat() {
        /*TODO "new UserId((userDTO.getUserId()))" > parametrize user id*/
        ChatId actualChatId = null;

        try {
            final ChatCreationDto chatCreationDto = new ChatCreationDto(new UserId((userDTO.getUserId())), chatName);
            actualChatId = chatService.createChat(chatCreationDto);
        } catch (ChatCreationException e) {
            assertNotNull("New chat was not created.");
        }

        assertNotNull(actualChatId);
    }

    @Test
    public void failAdditionNewChatWithEmptyInput() {

        try {
            chatService.createChat(new ChatCreationDto(new UserId((userDTO.getUserId())), ""));
            fail("Chat creation exception was not thrown");
        } catch (ChatCreationException e) {
            assertEquals("Chat creation fail message are not match",
                    e.getMessage(), EMPTY_CHAT_NAME.getMessage());
        }
    }

    @Test
    public void failAdditionNewChatWithAlreadyCreatedChatName() throws ChatCreationException {

        chatService.createChat(new ChatCreationDto(new UserId((userDTO.getUserId())), chatName));

        try {
            chatService.createChat(new ChatCreationDto(new UserId((userDTO.getUserId())), chatName));
            fail("Chat creation exception was not thrown");
        } catch (ChatCreationException e) {
            assertEquals("Chat creation fail message are not match",
                    e.getMessage(), NON_UNIQUE_CHAT_NAME.getMessage());
        }
    }

    @Test
    public void addDuplicateMemberToChatFail() throws MemberException {
        String chatName = "fishing";
        ChatId chatId = null;

        try {
            chatId = chatService.createChat(new ChatCreationDto(new UserId(userDTO.getUserId()), chatName));
        } catch (ChatCreationException e) {
            fail("Chat was not created.");
        }

        chatService.addMember(new MemberChatDto(new UserId(userDTO.getUserId()), chatId));

        try {
            chatService.addMember(new MemberChatDto(new UserId(userDTO.getUserId()), chatId));
        } catch (MemberException e) {
            assertEquals("Add chat member fail message are not match",
                    CHAT_MEMBER_ALREADY_JOIN.getMessage(), e.getMessage());
        }
    }

    @Test
    public void addMemberToChat() {
        String chatName = "fishing";
        ChatId chatId = null;
        try {
            chatId = chatService.createChat(new ChatCreationDto(new UserId((userDTO.getUserId())), chatName));
        } catch (ChatCreationException e) {
            fail("Chat creation failed.");
        }

        try {
            chatService.addMember(new MemberChatDto(new UserId(userDTO.getUserId()), chatId));
        } catch (MemberException e) {
            fail("Add member failed");
        }

    }

    @Test
    public void removeChatMember() {
        String chatName = "sport";
        ChatId chatId = null;
        try {
            chatId = chatService.createChat(new ChatCreationDto(new UserId((userDTO.getUserId())), chatName));
        } catch (ChatCreationException e) {
            fail("Chat creation failed.");
        }

        try {
            chatService.addMember(new MemberChatDto(new UserId(userDTO.getUserId()), chatId));
        } catch (MemberException e) {
            fail("Add member failed");
        }

        try {
            chatService.removeMember((new MemberChatDto(new UserId(userDTO.getUserId()), chatId)));
        } catch (MemberException e) {
            fail("Remove member failed");
        }
    }

    @Test
    public void removeChatMemberFail() {
        String chatName = "sport";
        ChatId chatId = null;
        try {
            chatId = chatService.createChat(new ChatCreationDto(new UserId((userDTO.getUserId())), chatName));
        } catch (ChatCreationException e) {
            fail("Chat creation failed.");
        }
        try {
            chatService.removeMember((new MemberChatDto(new UserId(userDTO.getUserId()), chatId)));
        } catch (MemberException e) {
            assertEquals("Add chat member fail message are not match", NOT_A_CHAT_MEMBER.getMessage(), e.getMessage());
        }
    }

}
