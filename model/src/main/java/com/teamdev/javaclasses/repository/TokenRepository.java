package com.teamdev.javaclasses.repository;

import com.teamdev.javaclasses.entities.SecurityToken;
import com.teamdev.javaclasses.entities.TokenId;
import com.teamdev.javaclasses.entities.UserId;

import java.util.Collection;

/**
 * Implementation {@link InMemoryRepository} for SecurityToken entity keeping.
 */
public class TokenRepository extends InMemoryRepository<SecurityToken, TokenId> {

    @Override
    TokenId getNextId() {
        return new TokenId(System.nanoTime());
    }

    public SecurityToken get(UserId userId) {

        final Collection<SecurityToken> allTokens = findAll();
        SecurityToken accessToken = null;

        for (SecurityToken currentToken : allTokens) {
            if (userId.equals(currentToken.getUserId())) {
                accessToken = currentToken;
                break;

            }
        }
        return accessToken;

    }

    public UserId get(SecurityToken accessToken) {

        final Collection<SecurityToken> allTokens = findAll();
        UserId userId = null;

        for (SecurityToken currentToken : allTokens) {
            if (accessToken.getId().equals(currentToken.getId())) {
                userId = currentToken.getUserId();
                break;

            }
        }
        return userId;

    }
}