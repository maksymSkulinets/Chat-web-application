package com.teamdev.javaclasses.dto;

/**
 * Unique token for access to system.
 */
public class TokenDTO {

    private Long token;
    private Long userId;

    public TokenDTO(Long token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public Long getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenDTO that = (TokenDTO) o;

        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        return userId != null ? userId.equals(that.userId) : that.userId == null;

    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
