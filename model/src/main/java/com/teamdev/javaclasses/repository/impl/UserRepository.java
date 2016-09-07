package com.teamdev.javaclasses.repository.impl;

import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.tinyTypes.UserId;
import com.teamdev.javaclasses.repository.InMemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation {@link InMemoryRepository} for User entity keeping.
 */
public class UserRepository extends InMemoryRepository<User, UserId> {
    private static UserRepository userRepository = UserRepository.getInstance();
    private final AtomicLong idCounter = new AtomicLong(0);

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    @Override
    public UserId getNextId() {
        return new UserId(idCounter.getAndIncrement());
    }

    public User getUser(String nickname) {
        final Collection<User> allUsers = findAll();
        User user = null;

        for (User currentUser : allUsers) {
            if (currentUser.getNickname().getName().equals(nickname)) {
                user = currentUser;
                break;
            }
        }

        return user;
    }

}
