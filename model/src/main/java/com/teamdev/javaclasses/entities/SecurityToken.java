
package com.teamdev.javaclasses.entities;

/**
 * Unique token for access to system.
 */
public class SecurityToken implements Entity<TokenId> {
    private TokenId tokenId;
    private UserId userId;

    public SecurityToken(TokenId tokenId, UserId userId) {
        this.tokenId = tokenId;
        this.userId = userId;
    }

    public SecurityToken(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SecurityToken that = (SecurityToken) o;

        if (tokenId != null ? !tokenId.equals(that.tokenId) : that.tokenId != null) return false;
        return userId != null ? userId.equals(that.userId) : that.userId == null;

    }

    @Override
    public int hashCode() {
        int result = tokenId != null ? tokenId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    @Override
    public TokenId getId() {
        return tokenId;
    }

    @Override
    public void setId(TokenId id) {
        tokenId = id;
    }
}
