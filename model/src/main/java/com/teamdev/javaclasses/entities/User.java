package com.teamdev.javaclasses.entities;

import com.teamdev.javaclasses.entities.tinyTypes.Password;
import com.teamdev.javaclasses.entities.tinyTypes.UserId;
import com.teamdev.javaclasses.entities.tinyTypes.UserName;

/**
 * User entity.
 */
public class User implements Entity<UserId> {
    private final UserName nickname;
    private final Password password;
    private UserId id;

    public User(UserName nickname, Password password) {
        this.nickname = nickname;
        this.password = password;
    }

    public UserName getNickname() {
        return nickname;
    }

    public Password getPassword() {
        return password;
    }

    public UserId getId() {
        return id;
    }

    @Override
    public void setId(UserId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (nickname != null ? !nickname.equals(user.nickname) : user.nickname != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        return id != null ? id.equals(user.id) : user.id == null;

    }

    @Override
    public int hashCode() {
        int result = nickname != null ? nickname.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
