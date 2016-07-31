import com.teamdev.javaclasses.ChatCreationException;
import com.teamdev.javaclasses.ChatService;
import com.teamdev.javaclasses.DTO.*;
import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.impl.ChatServiceImpl;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.teamdev.javaclasses.ChatFailCases.EMPTY_CHAT_NAME;
import static com.teamdev.javaclasses.ChatFailCases.NON_UNIQUE_CHAT_NAME;

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
            Assert.fail("Sign up with valid data is not successful");
        }
        return result;
    }

    private SecurityTokenDTO successfulLogin(LoginDTO loginData) {
        SecurityTokenDTO result = null;
        try {
            result = userService.login(loginData);
        } catch (LoginException e) {
            Assert.fail("Sign up with valid data is not successful");
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
            Assert.assertNotNull("New chat was not created.");
        }

        Assert.assertNotNull(actualChatId);
    }

    @Test
    public void failAdditionNewChatWithEmptyInput() {

        try {
            chatService.createChat(new ChatCreationDto(new UserId((userDTO.getUserId())), ""));
            Assert.fail("Chat creation exception was not thrown");
        } catch (ChatCreationException e) {
            Assert.assertEquals("Chat creation fail message are not match",
                    e.getMessage(), EMPTY_CHAT_NAME.getMessage());
        }
    }

    @Test
    public void failAdditionNewChatWithAlreadyCreatedChatName() throws ChatCreationException {

        chatService.createChat(new ChatCreationDto(new UserId((userDTO.getUserId())), chatName));

        try {
            chatService.createChat(new ChatCreationDto(new UserId((userDTO.getUserId())), chatName));
            Assert.fail("Chat creation exception was not thrown");
        } catch (ChatCreationException e) {
            Assert.assertEquals("Chat creation fail message are not match",
                    e.getMessage(), NON_UNIQUE_CHAT_NAME.getMessage());
        }
    }


}
