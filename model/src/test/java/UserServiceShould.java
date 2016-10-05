import com.teamdev.javaclasses.service.LoginException;
import com.teamdev.javaclasses.service.SignUpException;
import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.service.UserService;
import com.teamdev.javaclasses.service.impl.UserServiceImpl;
import org.junit.Test;

import java.util.Optional;

import static com.teamdev.javaclasses.service.UserServiceFailCases.*;
import static org.junit.Assert.*;

public class UserServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();
    private String nickname = "Darth_Wader";
    private String password = "Luk,i_am_your_father";

    @Test
    public void signUpUser() throws SignUpException {

        final UserDto user = userService
                .signUp(new SignUpDto(nickname, password, password));
        final Optional<UserDto> userById = userService.findUser(
                new UserIdDto(user.getId()));

        assertTrue("Current user is not keep in repository.", userById.isPresent());

        assertEquals("Nickname of registered user is not equal expected.",
                nickname, userById.get().getNickname());

        userService.deleteAccount(new UserIdDto(userById.get().getId()));
    }


    @Test
    public void duplicateUserSignUpFail() throws SignUpException {

        final UserDto user = userService.signUp(new SignUpDto(nickname, password, password));

        try {
            userService.signUp(new SignUpDto(nickname, password, password));
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            assertEquals("Sign up fail messages are not match", EXIST_USER.getMessage(), e.getMessage());
        }

        userService.deleteAccount(new UserIdDto(user.getId()));

    }

    @Test
    public void passwordsNonMatchingSignUpFail() {

        try {
            userService.signUp(new SignUpDto(nickname, password, "NOT_MATCH"));
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            assertEquals("Sign up fail messages are not match", PASSWORDS_NOT_MATCH.getMessage(), e.getMessage());
        }
    }

    @Test
    public void emptyInputSignUpFail() {

        try {
            userService.signUp(new SignUpDto(nickname, "", password));
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            assertEquals("Sign up with not filled input", EMPTY_INPUT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void loginUser() throws SignUpException, LoginException {

        final UserDto user = userService.signUp(new SignUpDto(nickname, password, password));
        final TokenDto userToken = userService.login(new LoginDto(nickname, password));
        final Optional<UserDto> userByToken = userService.findUser(new UserIdDto(userToken.getUserId()));

        assertTrue("User token is not keep in repository but was logged.", userByToken.isPresent());

        assertEquals("User with current nickname is not logged.",
                nickname, userByToken.get().getNickname());

        userService.deleteAccount(new UserIdDto(user.getId()));
    }

    @Test
    public void emptyInputLoginFail() throws SignUpException, LoginException {

        final UserDto user = userService.signUp(new SignUpDto(nickname, password, password));

        try {
            userService.login(new LoginDto("", password));
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            assertEquals("Sign up fail messages are not match", EMPTY_INPUT.getMessage(), e.getMessage());
        }

        userService.deleteAccount(new UserIdDto(user.getId()));
    }

    @Test
    public void nonSignUpUserLoginFail() throws SignUpException {

        try {
            userService.login(new LoginDto(nickname, password));
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            assertEquals("Login fail messages are not match",
                    NON_SIGN_UP_USER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void loginWithWrongPasswordFail() throws SignUpException {

        final UserDto user = userService.signUp(new SignUpDto(nickname, password, password));

        try {
            userService.login(new LoginDto(nickname, "_WRONG_PASS"));
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            assertEquals("User with non correct password was login",
                    NON_SIGN_UP_USER.getMessage(), e.getMessage());
        }

        userService.deleteAccount(new UserIdDto(user.getId()));
    }

    @Test
    public void removeUser() throws SignUpException, LoginException {

        userService.signUp(new SignUpDto(nickname, password, password));
        final TokenDto userToken = userService.login(new LoginDto(nickname, password));
        final Optional<UserDto> user = userService.findUser(new TokenIdDto(userToken.getToken()));
        assertTrue("Current user entity is not keep in repository.", user.isPresent());

        userService.deleteAccount(new UserIdDto(userToken.getUserId()));
        final Optional<UserDto> deletedUser = userService.findUser(new TokenIdDto(userToken.getToken()));
        assertFalse("Current user entity keep in repository but was deleted.", deletedUser.isPresent());
    }

    @Test
    public void logoutUser() throws SignUpException, LoginException {

        userService.signUp(new SignUpDto(nickname, password, password));
        final TokenDto userToken = userService.login(new LoginDto(nickname, password));
        final Optional<UserDto> userByToken = userService.findUser(new TokenIdDto(userToken.getToken()));
        assertTrue("Current user entity is not keep in repository.", userByToken.isPresent());

        userService.logout(new TokenIdDto(userToken.getToken()));
        final Optional<UserDto> logoutUser = userService.findUser(new TokenIdDto(userToken.getToken()));
        assertFalse("Current user token keep in repository but user was logout.", logoutUser.isPresent());

        userService.deleteAccount(new UserIdDto(userToken.getUserId()));
    }
}