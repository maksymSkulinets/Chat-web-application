package com.teamdev.javaclasses.DTO;

public class SignUpDTO {
    private String nickname;
    private String password;
    private String verifyPassword;

    public SignUpDTO(String nickname, String password, String verifyPassword) {
        this.nickname = nickname;
        this.password = password;
        this.verifyPassword = verifyPassword;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignUpDTO signUpDTO = (SignUpDTO) o;

        if (nickname != null ? !nickname.equals(signUpDTO.nickname) : signUpDTO.nickname != null) return false;
        if (password != null ? !password.equals(signUpDTO.password) : signUpDTO.password != null) return false;
        return verifyPassword != null ? verifyPassword.equals(signUpDTO.verifyPassword) : signUpDTO.verifyPassword == null;

    }

    @Override
    public int hashCode() {
        int result = nickname != null ? nickname.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (verifyPassword != null ? verifyPassword.hashCode() : 0);
        return result;
    }

}
