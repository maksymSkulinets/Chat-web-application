package com.teamdev.javaclasses.repository;

import com.teamdev.javaclasses.DTO.SecurityTokenDTO;
import com.teamdev.javaclasses.entities.SecurityToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation {@link InMemoryRepository} for SecurityTokenDTO entity keeping.
 */
public class TokenRepository extends InMemoryRepository<SecurityTokenDTO, SecurityToken> {
    private static final TokenRepository tokenRepository = new TokenRepository();
    private final Logger log = LoggerFactory.getLogger(TokenRepository.class);

    private TokenRepository() {
    }

    public static TokenRepository getInstance() {
        return tokenRepository;
    }


    @Override
    SecurityToken getNextId() {
        if (log.isDebugEnabled()) {
            log.debug("Security token produce");
        }
        return new SecurityToken(System.nanoTime());
    }

    public void remove(SecurityTokenDTO token) {

    }
}