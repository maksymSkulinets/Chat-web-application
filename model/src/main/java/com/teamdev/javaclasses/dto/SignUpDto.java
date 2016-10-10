package com.teamdev.javaclasses.dto;

/**
 * Data transfer object for sign up personal information.
 */
public class SignUpDto {
    private final String nickname;
    private final String password;
    private final String verifyPassword;

    public SignUpDto(String nickname, String password, String verifyPassword) {
        this.nickname = nickname;
        this.password = password;
        this.verifyPassword = verifyPassword;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignUpDto signUpDto = (SignUpDto) o;

        if (nickname != null ? !nickname.equals(signUpDto.nickname) : signUpDto.nickname != null) return false;
        if (password != null ? !password.equals(signUpDto.password) : signUpDto.password != null) return false;
        return verifyPassword != null ? verifyPassword.equals(signUpDto.verifyPassword) : signUpDto.verifyPassword == null;

    }

    @Override
    public int hashCode() {
        int result = nickname != null ? nickname.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (verifyPassword != null ? verifyPassword.hashCode() : 0);
        return result;
    }

}
