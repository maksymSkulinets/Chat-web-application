package com.teamdev.javaclasses.repository;

/**
 * Implementation {@link InMemoryRepository} for User entity keeping.
 */
public class UserRepository<User, UserId> extends InMemoryRepository<User, UserId> {
    private final static UserRepository userRepository = new UserRepository();

    private UserRepository() {
        super();
    }

    public static UserRepository getInstance() {
        return userRepository;
    }
}
