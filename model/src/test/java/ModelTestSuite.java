import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserServiceShould.class,
        ChatServiceShould.class,
        MultithreadingTest.class
})

public class ModelTestSuite {
}

