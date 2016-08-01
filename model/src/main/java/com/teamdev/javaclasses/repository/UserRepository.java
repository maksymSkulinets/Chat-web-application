package com.teamdev.javaclasses.repository;

import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation {@link InMemoryRepository} for User entity keeping.
 */
public class UserRepository extends InMemoryRepository<User, UserId> {
    private static final UserRepository userRepository = new UserRepository();
    private final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final AtomicLong idCounter = new AtomicLong(0);

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        return userRepository;
    }

    @Override
    UserId getNextId() {

        final UserId userId = new UserId(idCounter.getAndIncrement());
        if (log.isDebugEnabled()) {
            log.debug("User idCounter with value: " + userId.getValue() + " produce.");
        }
        return userId;
    }

    public User get(String nickname) {
        final Collection<User> allUsers = findAll();
        User user = null;

        for (User currentUser : allUsers) {
            if (currentUser.getNickname().equals(nickname)) {
                user = currentUser;
                break;
            }
        }

        return user;
    }

    public UserId get(User user) {
        final Collection<User> allUsers = findAll();
        UserId userId = null;

        for (User currentUser : allUsers) {
            if (currentUser.getNickname().equals(user.getNickname())
                    && currentUser.getPassword().equals(user.getPassword())) {
                userId = currentUser.getId();
                break;
            }
        }

        return userId;
    }
}
