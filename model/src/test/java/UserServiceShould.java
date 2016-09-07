import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.UserService;
import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.Callable;

import static com.teamdev.javaclasses.UserServiceFailCases.*;
import static org.junit.Assert.*;

public class UserServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();
    private String nickname;
    private String password;

    @Test
    public void signUpUser() throws SignUpException {
        nickname = "Luk";
        password = "qwerty";

        final UserDto user = userService
                .signUp(new SignUpDto(nickname, password, password));

        final Optional<UserDto> userById = userService.findUser(
                new UserIdDto(user.getId()));

        assertTrue("Current user is not keep in repository.",userById.isPresent());

        assertEquals("Nickname of registered user is not equal expected.",
                nickname, userById.get().getNickname());

    }


    @Test
    public void failInDuplicateUserSignUp() throws SignUpException {
        nickname = "Bill";
        password = "just_young_alcoholic";

        userService.signUp(new SignUpDto(nickname, password, password));

        try {
            userService.signUp(new SignUpDto(nickname, password, password));
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            assertEquals("Sign up fail messages are not match", EXIST_USER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void passwordsNonMatchingSignUpFail() {
        nickname = "DartWader";
        password = "Luk_i_am_you_father!";

        try {
            userService.signUp(new SignUpDto(nickname, password, password + "NO!!!"));
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            assertEquals("Sign up fail messages are not match", PASSWORDS_NOT_MATCH.getMessage(), e.getMessage());
        }
    }

    @Test
    public void emptyInputSignUpFail() {
        nickname = "unknown";
        password = "";

        try {
            userService.signUp(new SignUpDto(nickname, password, password));
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            assertEquals("Sign up with not filled input", EMPTY_INPUT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void loginUser() throws SignUpException, LoginException {
        nickname = "Anna";
        password = "anna_password";

        final UserDto actualUserDto = userService.signUp(new SignUpDto(nickname, password, password));
        final TokenDto actualTokenDto = userService.login(new LoginDto(nickname, password));
        final Optional<UserDto> actualUser = userService.findUser(new UserIdDto(actualTokenDto.getUserId()));

        assertTrue("Current user is not keep in repository.",actualUser.isPresent());

        assertEquals("User ID after registration and user ID after login are difference ,",
                actualUserDto.getId(), actualTokenDto.getUserId());

        assertEquals("User with current nickname is not login",
                nickname, actualUser.get().getNickname());
    }

    @Test
    public void emptyInputLoginFail() throws SignUpException, LoginException {
        nickname = "Peter";
        password = "piece_of_text";

        final SignUpDto signUpData = new SignUpDto(nickname, password, password);
        final LoginDto loginData = new LoginDto("", password);

        userService.signUp(signUpData);
        try {
            userService.login(loginData);
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            assertEquals("Sign up fail messages are not match", EMPTY_INPUT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void nonSignUpUserLoginFail() throws SignUpException {
        nickname = "Joan";
        password = "i_am_not_sign_up_yet";

        final LoginDto loginData = new LoginDto(nickname, password);

        try {
            userService.login(loginData);
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            assertEquals("Login fail messages are not match",
                    NON_SIGN_UP_USER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void loginWithWrongPasswordFail() throws SignUpException {
        nickname = "John";
        password = "you_shall_not_pass";

        userService.signUp(new SignUpDto(nickname, password, password));

        try {
            userService.login(new LoginDto(nickname, password + "_WRONG!"));
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            assertEquals("User with non correct password was login",
                    NON_SIGN_UP_USER.getMessage(), e.getMessage());

        }
    }

    @Test
    public void deleteUser() throws SignUpException, LoginException {
        nickname = "Elizabet";
        password = "elizabet_password";

        userService.signUp(new SignUpDto(nickname, password, password));
        final TokenDto userToken = userService.login(new LoginDto(nickname, password));
        final Optional<UserDto> user = userService.findUser(new TokenIdDto(userToken.getToken()));
        assertTrue("Current user is not keep in repository.", user.isPresent());

        userService.deleteUser(new UserIdDto(userToken.getUserId()));
        final Optional<UserDto> notExistUser = userService.findUser(new TokenIdDto(userToken.getToken()));
        assertFalse("Current user keep in repository but was deleted.", notExistUser.isPresent());
        System.out.println(notExistUser.isPresent());
    }


}