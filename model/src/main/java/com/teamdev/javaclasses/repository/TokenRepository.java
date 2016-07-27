package com.teamdev.javaclasses.repository;

import com.teamdev.javaclasses.entities.AccessToken;
import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.UserId;

public class TokenRepository extends InMemoryRepository<AccessToken, UserId> {
    private final static TokenRepository tokenRepository = new TokenRepository();

    private TokenRepository() {
    }

    public static TokenRepository getInstance() {
        return tokenRepository;
    }
}
