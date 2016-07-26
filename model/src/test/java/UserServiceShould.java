import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.UserService;
import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import com.teamdev.javaclasses.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;

public class UserServiceShould {


    @Test
    public void signUpUser() throws SignUpException {
        User expectedUser = new User("Mike", "you_shall_not_pass", new UserId(0));

        UserRepository userRepository = UserRepository.getInstance();
        UserService userService = new UserServiceImpl(userRepository);

        UserId userId = userService
                .signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());

        User actualUser = (User) userRepository.read(userId);

        Assert.assertEquals("User with current nickname is not registered",
                expectedUser.getNickname(), actualUser.getNickname());
        Assert.assertEquals("User with current password is not registered",
                expectedUser.getPassword(), actualUser.getPassword());

    }

    @Test
    public void failDuplicateUserSignUp() throws SignUpException {
        User expectedUser = new User("Anna", "you_shall_not_pass", new UserId(1));

        UserRepository userRepository = UserRepository.getInstance();
        UserService userService = new UserServiceImpl(userRepository);

        userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());

        try {
            userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), expectedUser.getPassword());
        } catch (SignUpException e) {
            Assert.assertEquals("Sign up fail messages are not match", "Current nickname must be unique", e.getMessage());
        }
        Assert.fail("User duplicate signUp was success");

    }

    @Test
    public void failPasswordMatching() throws SignUpException {
        User expectedUser = new User("John", "you_shall_not_pass", new UserId(2));

        UserRepository userRepository = UserRepository.getInstance();
        UserService userService = new UserServiceImpl(userRepository);

        try {
            userService.signUp(expectedUser.getNickname(), expectedUser.getPassword(), "not_match_password");
        } catch (SignUpException e) {
            Assert.assertEquals("Sign up fail messages are not match", "Passwords must be matched", e.getMessage());
        }
        Assert.fail("User with not matching password signUp was success");

    }

}
