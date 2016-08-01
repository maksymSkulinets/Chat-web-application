import com.teamdev.javaclasses.DTO.LoginDTO;
import com.teamdev.javaclasses.DTO.SecurityTokenDTO;
import com.teamdev.javaclasses.DTO.SignUpDTO;
import com.teamdev.javaclasses.DTO.UserDTO;
import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.entities.SecurityToken;
import com.teamdev.javaclasses.entities.User;
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

    private final UserServiceImpl userService = UserServiceImpl.getInstance();
    private String nickname;
    private String password;
    private Callable<SecurityTokenDTO> securityTokenDTOCallable;

    @Test
    public void signUpUser() throws SignUpException {
        nickname = "Luk";
        password = "qwerty";

        final UserDTO actualUserDTO = userService
                .signUp(new SignUpDTO(nickname, password, password));

        final User actualUser = userService.getUser(new UserId(actualUserDTO.getUserId()));

        assertEquals("User with current nickname is not registered",
                nickname, actualUserDTO.getNickname());
        assertEquals("User with current password is not registered",
                password, actualUser.getPassword());

        assertEquals("Id keep in server storage and id which return are different",
                actualUserDTO.getUserId(), actualUser.getId().getValue());
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

        final UserDTO currentUserDTO = userService.signUp(new SignUpDTO(nickname, password, password));
        final SecurityTokenDTO currentTokenDTO = userService.login(new LoginDTO(nickname, password));
        final User actualUser = userService.getUser(currentTokenDTO.getUserId());

        assertEquals("User ID after registration and user ID after login are difference ,",
                currentUserDTO.getUserId(), currentTokenDTO.getUserId().getValue());
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
        nickname = "Mike";
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
        final Set<SecurityToken> tokens = new HashSet<>();

        for (Future<SecurityTokenDTO> future : results) {
            userIds.add(future.get().getUserId());
            tokens.add(future.get().getId());
        }

        if (userIds.size() != count) {
            fail("Generated user ids are not unique");
        }

        if (tokens.size() != count) {
            fail("Generated tokens are not unique");
        }

    }
}