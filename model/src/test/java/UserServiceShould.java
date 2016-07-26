import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.UserService;
import com.teamdev.javaclasses.entities.AccessToken;
import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import com.teamdev.javaclasses.repository.TokenRepository;
import com.teamdev.javaclasses.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class UserServiceShould {
    final UserRepository userRepository = UserRepository.getInstance();
    final TokenRepository tokenRepository = TokenRepository.getInstance();
    final UserService userService = new UserServiceImpl(userRepository, tokenRepository);

    @Test
    public void signUpUser() throws SignUpException {
        User expectedUser = new User("Mike", "you_shall_not_pass");

        UserId userId = userService
                .signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());

        User actualUser = (User) userRepository.readType(userId);

        Assert.assertEquals("User with current nickname is not registered",
                expectedUser.getNickname(), actualUser.getNickname());
        Assert.assertEquals("User with current password is not registered",
                expectedUser.getPassword(), actualUser.getPassword());
    }

    @Test
    public void failDuplicateUserSignUp() throws SignUpException {
        User expectedUser = new User("Anna", "you_shall_not_pass");

        userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());

        try {
            userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            Assert.assertEquals("Sign up fail messages are not match", "Current nickname must be unique", e.getMessage());
        }
    }

    @Test
    public void failPasswordMatchingSignUp() {
        User expectedUser = new User("John", "you_shall_not_pass");

        try {
            userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), "not_match_password");
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            Assert.assertEquals("Sign up fail messages are not match", "Passwords must match", e.getMessage());
        }
    }

    @Test
    public void failEmptyInputSignUp() {
        User expectedUser = new User("   ", "you_shall_not_pass");

        try {
            userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), "");
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            Assert.assertEquals("Sign up with not filled input", "All fields must be filled", e.getMessage());
        }
    }

    @Test
    public void loginUser() throws SignUpException, LoginException {
        final User expectedUser = new User("Steve", "it_is_steve_password");

        final UserId userIdExpected = userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());
        final AccessToken token = userService.login(expectedUser.getNickname(), expectedUser.getPassword());

        final UserId userIdActual = (UserId) tokenRepository.readId(token);
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
            Assert.assertEquals("Sign up fail messages are not match", "All fields must be filled", e.getMessage());
        }
    }

    @Test
    public void notSignUpLoginFail() throws SignUpException {
        final User expectedUser = new User("Paul", "paul_password");

        try {
            userService.login("another_user", "another_password");
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            Assert.assertEquals("Login fail messages are not match", "Such user must register before", e.getMessage());
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
            Assert.assertEquals("User with non correct password was login", "Such user must register before", e.getMessage());
        }
    }


}