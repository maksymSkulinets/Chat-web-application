package com.teamdev.javaclasses.repository;

import com.teamdev.javaclasses.DTO.SecurityTokenDTO;
import com.teamdev.javaclasses.entities.SecurityToken;
import com.teamdev.javaclasses.entities.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Implementation {@link InMemoryRepository} for SecurityTokenDTO entity keeping.
 */
public class TokenRepository extends InMemoryRepository<SecurityTokenDTO, SecurityToken> {

    private final Logger log = LoggerFactory.getLogger(TokenRepository.class);

    @Override
    SecurityToken getNextId() {
        if (log.isDebugEnabled()) {
            log.debug("Security token produce");
        }
        return new SecurityToken(System.nanoTime());
    }

    public SecurityTokenDTO get(UserId userId) {

        final Collection<SecurityTokenDTO> allTokens = findAll();
        SecurityTokenDTO accessToken = null;

        for (SecurityTokenDTO currentToken : allTokens) {
            if (userId.equals(currentToken.getUserId())) {
                accessToken = currentToken;
                break;

            }
        }
        return accessToken;

    }

    public UserId get(SecurityTokenDTO accessToken) {

        final Collection<SecurityTokenDTO> allTokens = findAll();
        UserId userId = null;

        for (SecurityTokenDTO currentToken : allTokens) {
            if (accessToken.getValue().equals(currentToken.getValue())) {
                userId = currentToken.getUserId();
                break;

            }
        }
        return userId;

    }
}