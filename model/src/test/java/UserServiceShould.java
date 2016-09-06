import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.UserService;
import com.teamdev.javaclasses.entities.TokenId;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.teamdev.javaclasses.UserServiceFailCases.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();
    private String nickname;
    private String password;
    private Callable<SecurityTokenDTO> securityTokenDTOCallable;

    @Test
    public void signUpUser() throws SignUpException {
        nickname = "Luk";
        password = "qwerty";

        final UserDTO expectedUser = userService
                .signUp(new SignUpDTO(nickname, password, password));

        final UserDTO actualUser  = userService.findUser(
                new UserIdDTO(expectedUser.getId()));

        assertEquals("User with current nickname is not registered",
                nickname, actualUser.getNickname());

        assertEquals("Id keep in server storage and id which return are different",
                actualUser.getId(), expectedUser.getId());
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
            assertEquals("Sign up fail messages are not match", EXIST_USER.getMessage(), e.getMessage());
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
            assertEquals("Sign up fail messages are not match", PASSWORDS_NOT_MATCH.getMessage(), e.getMessage());
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
            assertEquals("Sign up with not filled input", EMPTY_INPUT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void loginUser() throws SignUpException, LoginException {
        nickname = "Anna";
        password = "anna_password";

        final UserDTO actualUserDTO = userService.signUp(new SignUpDTO(nickname, password, password));
        final SecurityTokenDTO actualTokenDTO = userService.login(new LoginDTO(nickname, password));
        final UserDTO actualUser = userService.findUser(new UserIdDTO(actualTokenDTO.getUserId()));

        assertEquals("User ID after registration and user ID after login are difference ,",
                actualUserDTO.getId(), actualTokenDTO.getUserId());
        assertEquals("User with current nickname is not login",
                nickname, actualUser.getNickname());
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
            assertEquals("Sign up fail messages are not match", EMPTY_INPUT.getMessage(), e.getMessage());
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
                    NON_SIGN_UP_USER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void loginWithWrongPasswordFail() throws SignUpException {
        nickname = "John";
        password = "you_shall_not_pass";

        userService.signUp(new SignUpDTO(nickname, password, password));

        try {
            userService.login(new LoginDTO(nickname, password + "_WRONG!"));
            fail("LoginException was not thrown");
        } catch (LoginException e) {
            assertEquals("User with non correct password was login",
                    NON_SIGN_UP_USER.getMessage(), e.getMessage());

        }
    }

    @Test
    public void multiThreadingSupportTest() throws ExecutionException, InterruptedException {

        final int count = 100;
        final ExecutorService executor = Executors.newFixedThreadPool(count);
        final CountDownLatch startLatch = new CountDownLatch(count);
        final List<Future<SecurityTokenDTO>> results = new ArrayList<>();
        AtomicInteger number = new AtomicInteger(0);

        Callable<SecurityTokenDTO> callable = () -> {

            startLatch.countDown();
            startLatch.await();

            final String nickname = "nickname" + number.get();
            final String password = "password" + number.getAndIncrement();

            userService.signUp(new SignUpDTO(nickname, password, password));
            return userService.login(new LoginDTO(nickname, password));
        };

        for (int i = 0; i < count; i++) {

            Future<SecurityTokenDTO> future = executor.submit(callable);
            results.add(future);
        }

        final Set<UserId> userIds = new HashSet<>();
        final Set<TokenId> tokens = new HashSet<>();

        for (Future<SecurityTokenDTO> future : results) {
            userIds.add(new UserId(future.get().getUserId()));
            tokens.add(new TokenId(future.get().getToken()));
        }

        if (userIds.size() != count) {
            fail("Generated user ids are not unique");
        }

        if (tokens.size() != count) {
            fail("Generated tokens are not unique");
        }

    }
}