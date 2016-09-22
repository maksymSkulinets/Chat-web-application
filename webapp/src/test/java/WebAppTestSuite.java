import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserServiceHandlersShould.class,
        ChatServiceHandlersShould.class
})

public class WebAppTestSuite {
}
