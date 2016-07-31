package com.teamdev.javaclasses.DTO;

/**
 * Unique user id and user nickname
 */
public class UserDTO {
    private final String nickname;
    private final long userId;

    public UserDTO(long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        if (userId != userDTO.userId) return false;
        return nickname != null ? nickname.equals(userDTO.nickname) : userDTO.nickname == null;

    }

    @Override
    public int hashCode() {
        int result = nickname != null ? nickname.hashCode() : 0;
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        return result;
    }
}
