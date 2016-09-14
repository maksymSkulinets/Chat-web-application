package com.teamdev.javaclasses.repository.impl;

import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.tinyTypes.UserId;
import com.teamdev.javaclasses.repository.InMemoryRepository;

import java.util.Collection;
import java.util.Optional;
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

    public Optional<User> getUser(String nickname) {
        final Collection<User> allUsers = findAll();
        Optional<User> result = Optional.empty();

        for (User currentUser : allUsers) {
            if (currentUser.getNickname().getValue().equals(nickname)) {
                result = Optional.of(currentUser);
                break;
            }
        }

        return result;
    }

}
