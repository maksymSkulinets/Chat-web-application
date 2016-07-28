import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.LoginFailCases;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.SignUpFailCases;
import com.teamdev.javaclasses.entities.SecurityToken;
import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class UserServiceShould {

    private final UserServiceImpl userService = UserServiceImpl.getInstance();

    @Test
    public void signUpUser() throws SignUpException {
        final User expectedUser = new User("Mike", "you_shall_not_pass");

        final UserId actualUserId = userService
                .signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());

        final User actualUser = userService.getUser(actualUserId);

        Assert.assertEquals("User with current nickname is not registered",
                expectedUser.getNickname(), actualUser.getNickname());
        Assert.assertEquals("User with current password is not registered",
                expectedUser.getPassword(), actualUser.getPassword());
    }


    @Test
    public void failInDuplicateUserSignUp() throws SignUpException {
        final User expectedUser = new User("Anna", "you_shall_not_pass");

        userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());

        try {
            userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            Assert.assertEquals("Sign up fail messages are not match", SignUpFailCases.EXIST_USER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void passwordsNonMatchingSignUpFail() {
        final User expectedUser = new User("John", "John_password");

        try {
            userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), "not_match_password");
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            Assert.assertEquals("Sign up fail messages are not match", SignUpFailCases.PASSWORDS_NOT_MATCH.getMessage(), e.getMessage());
        }
    }

    @Test
    public void emptyInputSignUpFail() {
        final User expectedUser = new User("   ", "password");

        try {
            userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), "");
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            Assert.assertEquals("Sign up with not filled input", SignUpFailCases.EMPTY_INPUT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void loginUser() throws SignUpException, LoginException {
        final User expectedUser = new User("Steve", "it_is_steve_password");

        final UserId userIdExpected = userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());
        final SecurityToken token = userService.login(expectedUser.getNickname(), expectedUser.getPassword());

        final UserId userIdActual = userService.getUserId(token);

        Assert.assertEquals("User with correct input was not login", userIdExpected, userIdActual);
    }

    @Test
    public void emptyInputLoginFail() throws SignUpException, LoginException {
        final User expectedUser = new User("Paul", "paul_password");

        userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());
        try {
            userService.login(expectedUser.getNickname(), "");
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            Assert.assertEquals("Sign up fail messages are not match", LoginFailCases.EMPTY_INPUT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void nonSignUpUserLoginFail() throws SignUpException {

        try {
            userService.login("another_user", "another_password");
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            Assert.assertEquals("Login fail messages are not match", LoginFailCases.NON_SIGN_UP_USER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void loginWithWrongPasswordFail() throws SignUpException {
        final User expectedUser = new User("Irvin", "irvin_password");

        userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());
        try {
            userService.login(expectedUser.getNickname(), "wrong_password");
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            Assert.assertEquals("User with non correct password was login", LoginFailCases.NON_SIGN_UP_USER.getMessage(), e.getMessage());

        }
    }
}