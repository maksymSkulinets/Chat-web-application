package com.teamdev.javaclasses.repository;

/**
 * Implementation {@link InMemoryRepository} for User entity keeping.
 */
public class UserRepository extends InMemoryRepository {
    private final UserRepository userRepository = new UserRepository();

    private UserRepository() {
        super();
    }

    public UserRepository getInstance() {
        return userRepository;
    }
}
