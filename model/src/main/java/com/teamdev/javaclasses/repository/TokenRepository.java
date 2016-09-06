package com.teamdev.javaclasses.repository;

import com.teamdev.javaclasses.dto.TokenDTO;
import com.teamdev.javaclasses.entities.Token;
import com.teamdev.javaclasses.entities.tinyTypes.TokenId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation {@link InMemoryRepository} for TokenDTO entity keeping.
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
    TokenId getNextId() {
        if (log.isDebugEnabled()) {
            log.debug("Security token produce");
        }
        return new TokenId(System.nanoTime());
    }

    public void remove(TokenDTO token) {
        /*TODO implements*/
    }
}