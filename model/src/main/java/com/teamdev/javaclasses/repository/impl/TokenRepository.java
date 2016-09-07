package com.teamdev.javaclasses.repository.impl;

import com.teamdev.javaclasses.entities.Token;
import com.teamdev.javaclasses.entities.tinyTypes.TokenId;
import com.teamdev.javaclasses.entities.tinyTypes.UserId;
import com.teamdev.javaclasses.repository.InMemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Implementation {@link InMemoryRepository} for TokenDto entity keeping.
 */
public class TokenRepository extends InMemoryRepository<Token, TokenId> {
    private static TokenRepository tokenRepository = TokenRepository.getInstance();
    private final Logger log = LoggerFactory.getLogger(TokenRepository.class);

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
        if (log.isDebugEnabled()) {
            log.debug("Security token produce");
        }
        return new TokenId(System.nanoTime());
    }

    public TokenId findTokenId(UserId id) {
        final Collection<Token> allTokens = tokenRepository.findAll();
        TokenId tokenId = null;

        for (Token current : allTokens) {
            if (current.getUserId().equals(id)) {
                tokenId = current.getId();
                break;
            }
        }
        return tokenId;
    }
}