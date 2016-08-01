
package com.teamdev.javaclasses.DTO;

import com.teamdev.javaclasses.entities.Entity;
import com.teamdev.javaclasses.entities.SecurityToken;
import com.teamdev.javaclasses.entities.UserId;

/**
 * Unique token for access to system.
 */
public class SecurityTokenDTO implements Entity<SecurityToken> {
    private SecurityToken token;
    private UserId userId;

    public SecurityTokenDTO(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SecurityTokenDTO that = (SecurityTokenDTO) o;

        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        return userId != null ? userId.equals(that.userId) : that.userId == null;

    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    @Override
    public SecurityToken getId() {
        return token;
    }

    @Override
    public void setId(SecurityToken id) {
        token = id;
    }
}
