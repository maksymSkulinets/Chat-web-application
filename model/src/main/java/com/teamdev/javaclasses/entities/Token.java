package com.teamdev.javaclasses.entities;

import com.teamdev.javaclasses.entities.tinyTypes.UserId;

public class Token implements Entity<TokenId> {
    private TokenId token;
    private UserId userId;

    public Token(UserId userId) {
        this.userId = userId;
    }

    @Override
    public TokenId getId() {
        return token;
    }

    @Override
    public void setId(TokenId id) {
        token = id;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token1 = (Token) o;

        if (token != null ? !token.equals(token1.token) : token1.token != null) return false;
        return userId != null ? userId.equals(token1.userId) : token1.userId == null;

    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
