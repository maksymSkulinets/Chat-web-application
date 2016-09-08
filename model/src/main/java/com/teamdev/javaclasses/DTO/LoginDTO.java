package com.teamdev.javaclasses.dto;

/**
 * Data transfer object for login personal information.
 */
public class LoginDto {
    private final String nickname;
    private final String password;

    public LoginDto(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginDto loginDto = (LoginDto) o;

        if (nickname != null ? !nickname.equals(loginDto.nickname) : loginDto.nickname != null) return false;
        if (password != null ? !password.equals(loginDto.password) : loginDto.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nickname != null ? nickname.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

}