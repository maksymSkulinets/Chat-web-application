import com.teamdev.javaclasses.DTO.LoginDTO;
import com.teamdev.javaclasses.DTO.SignUpDTO;
import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.LoginFailCases;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.SignUpFailCases;
import com.teamdev.javaclasses.DTO.SecurityTokenDTO;
import com.teamdev.javaclasses.entities.SecurityToken;
import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class UserServiceShould {

    private final UserServiceImpl userService = UserServiceImpl.getInstance();
    private String nickname;
    private String password;

    @Test
    public void signUpUser() throws SignUpException {
        nickname = "Luk";
        password = "qwerty";

        final UserId actualUserId = userService
                .signUp(new SignUpDTO(nickname, password, password));

        final User actualUser = userService.getUser(actualUserId);

        assertEquals("User with current nickname is not registered",
                nickname, actualUser.getNickname());
        assertEquals("User with current password is not registered",
                password, actualUser.getPassword());
    }


    @Test
    public void failInDuplicateUserSignUp() throws SignUpException {
        nickname = "Bill";
        password = "just_young_alcoholic";

        userService.signUp(new SignUpDTO(nickname, password, password));

        try {
            userService.signUp(new SignUpDTO(nickname, password, password));
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            assertEquals("Sign up fail messages are not match", SignUpFailCases.EXIST_USER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void passwordsNonMatchingSignUpFail() {
        nickname = "DartWader";
        password = "Luk_i_am_you_father!";

        try {
            userService.signUp(new SignUpDTO(nickname, password, password + "NO!!!"));
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            assertEquals("Sign up fail messages are not match", SignUpFailCases.PASSWORDS_NOT_MATCH.getMessage(), e.getMessage());
        }
    }

    @Test
    public void emptyInputSignUpFail() {
        nickname = "unknown";
        password = "";

        try {
            userService.signUp(new SignUpDTO(nickname, password, password));
            fail("SignUpException was not thrown");
        } catch (SignUpException e) {
            assertEquals("Sign up with not filled input", SignUpFailCases.EMPTY_INPUT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void loginUser() throws SignUpException, LoginException {
        nickname = "Anna";
        password = "anna_password";

        userService.signUp(new SignUpDTO(nickname, password, password));
        final SecurityTokenDTO token = userService.login(new LoginDTO(nickname, password));
        final User actualUser = userService.getUser(token.getUserId());

        assertEquals("User with current nickname is not login",
                nickname, actualUser.getNickname());
        assertEquals("User with current password is not login",
                password, actualUser.getPassword());
    }

    @Test
    public void emptyInputLoginFail() throws SignUpException, LoginException {
        nickname = "Peter";
        password = "piece_of_text";

        final SignUpDTO signUpData = new SignUpDTO(nickname, password, password);
        final LoginDTO loginData = new LoginDTO("", password);

        userService.signUp(signUpData);
        try {
            userService.login(loginData);
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            assertEquals("Sign up fail messages are not match", LoginFailCases.EMPTY_INPUT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void nonSignUpUserLoginFail() throws SignUpException {
        nickname = "Joan";
        password = "i_am_not_sign_up_yet";

        final LoginDTO loginData = new LoginDTO(nickname, password);

        try {
            userService.login(loginData);
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            assertEquals("Login fail messages are not match",
                    LoginFailCases.NON_SIGN_UP_USER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void loginWithWrongPasswordFail() throws SignUpException {
        nickname = "Mike";
        password = "you_shall_not_pass";

        userService.signUp(new SignUpDTO(nickname, password, password));

        try {
            userService.login(new LoginDTO(nickname, password + "_WRONG!"));
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            assertEquals("User with non correct password was login",
                    LoginFailCases.NON_SIGN_UP_USER.getMessage(), e.getMessage());

        }
    }

    @Test
    public void multiThreadingSupportTest() throws ExecutionException, InterruptedException {
        final int threadsNumber = 50;

        final ExecutorService executorService = Executors.newFixedThreadPool(threadsNumber);
        final List<Future<SecurityTokenDTO>> loginResults = new ArrayList<>();
        final CountDownLatch startLatch = new CountDownLatch(threadsNumber);
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final ArrayList<UserId> userIds = new ArrayList<>();
        final ArrayList<SecurityToken> tokens = new ArrayList<>();


        Callable<SecurityTokenDTO> callable = () -> {

            final String nickName = "Steven_" + atomicInteger.get();
            final String password = "password_" + atomicInteger.getAndIncrement();

            startLatch.countDown();
            startLatch.await();

            final UserId userId = userService.signUp(new SignUpDTO(nickName, password, password));
            final SecurityTokenDTO token = userService.login(new LoginDTO(nickName, password));
            return token;
        };

        for (int i = 0; i < threadsNumber; i++) {
            Future<SecurityTokenDTO> future = executorService.submit(callable);
            loginResults.add(future);
        }
        int objectsCounter = 0;
        for (Future<SecurityTokenDTO> currentToken : loginResults) {
            objectsCounter++;
            userIds.add(currentToken.get().getUserId());
            tokens.add(currentToken.get().getValue());
            /*TODO  compare */

            if (tokens.size() != objectsCounter) {
                {
                    fail("Generated tokens are not unique");
                }
            }
        }

    }
}