import com.teamdev.javaclasses.service.UserService;
import com.teamdev.javaclasses.dto.LoginDto;
import com.teamdev.javaclasses.dto.SignUpDto;
import com.teamdev.javaclasses.dto.TokenDto;
import com.teamdev.javaclasses.entities.tinyTypes.TokenId;
import com.teamdev.javaclasses.entities.tinyTypes.UserId;
import com.teamdev.javaclasses.service.impl.UserServiceImpl;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.fail;

public class MultithreadingTest {
    private final UserService userService = UserServiceImpl.getInstance();

    @Ignore
    @Test
    public void SafeLoggingInMultithreadingTest() throws ExecutionException, InterruptedException {

        final int count = 100;
        final ExecutorService executor = Executors.newFixedThreadPool(count);
        final CountDownLatch startLatch = new CountDownLatch(count);
        final List<Future<TokenDto>> results = new ArrayList<>();
        AtomicInteger number = new AtomicInteger(0);

        Callable<TokenDto> callable = () -> {

            startLatch.countDown();
            startLatch.await();

            final String nickname = "nickname" + number.get();
            final String password = "password" + number.getAndIncrement();

            userService.signUp(new SignUpDto(nickname, password, password));
            return userService.login(new LoginDto(nickname, password));
        };

        for (int i = 0; i < count; i++) {

            Future<TokenDto> future = executor.submit(callable);
            results.add(future);
        }

        final Set<UserId> userIds = new HashSet<>();
        final Set<TokenId> tokens = new HashSet<>();

        for (Future<TokenDto> future : results) {
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
