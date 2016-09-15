package com.teamdev.javaclasses.repository.impl;

import com.teamdev.javaclasses.entities.Token;
import com.teamdev.javaclasses.entities.tinyTypes.TokenId;
import com.teamdev.javaclasses.entities.tinyTypes.UserId;
import com.teamdev.javaclasses.repository.InMemoryRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Implementation {@link InMemoryRepository} for TokenDto entity keeping.
 */
public class TokenRepository extends InMemoryRepository<Token, TokenId> {
    private static TokenRepository tokenRepository = TokenRepository.getInstance();

    private TokenRepository() {
    }

    public static TokenRepository getInstance() {
        if (tokenRepository == null) {
            tokenRepository = new TokenRepository();
        }
        return tokenRepository;
    }


    @Override
    public TokenId getNextId() {
        return new TokenId(System.nanoTime());
    }

    public Optional<TokenId> findTokenId(UserId id) {
        final Collection<Token> allTokens = tokenRepository.findAll();
        Optional<TokenId> result = Optional.empty();

        for (Token current : allTokens) {
            if (current.getUserId().equals(id)) {
                result = Optional.of(current.getId());
                break;
            }
        }
        return result;
    }
}