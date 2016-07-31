package com.teamdev.javaclasses.DTO;

/**
 * Data transfer object for login personal information.
 */
public class LoginDTO {
    private String nickname;
    private String password;

    public LoginDTO(String nickname, String password) {
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

        LoginDTO loginDTO = (LoginDTO) o;

        if (nickname != null ? !nickname.equals(loginDTO.nickname) : loginDTO.nickname != null) return false;
        if (password != null ? !password.equals(loginDTO.password) : loginDTO.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nickname != null ? nickname.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

}