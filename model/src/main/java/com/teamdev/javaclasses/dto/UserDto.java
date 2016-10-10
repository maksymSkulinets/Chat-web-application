package com.teamdev.javaclasses.dto;

/**
 * Data transfer object.Contains  user id and user nickname
 */
public class UserDto {
    private final String nickname;
    private final Long id;

    public UserDto(String nickname, Long id) {
        this.id = id;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDto userDto = (UserDto) o;

        if (nickname != null ? !nickname.equals(userDto.nickname) : userDto.nickname != null) return false;
        return id != null ? id.equals(userDto.id) : userDto.id == null;

    }

    @Override
    public int hashCode() {
        int result = nickname != null ? nickname.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
