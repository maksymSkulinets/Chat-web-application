package com.teamdev.javaclasses.repository;

import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.UserId;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation {@link InMemoryRepository} for User entity keeping.
 */
public class UserRepository extends InMemoryRepository<User, UserId> {
    private AtomicLong nextId = new AtomicLong(0);

    @Override
    UserId getNextId() {
        return new UserId(nextId.getAndIncrement());
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
