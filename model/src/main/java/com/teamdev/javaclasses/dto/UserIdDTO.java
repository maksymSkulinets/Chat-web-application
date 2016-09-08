package com.teamdev.javaclasses.dto;

public class UserIdDto {
    private final Long id;

    public UserIdDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserIdDto userIdDto = (UserIdDto) o;

        if (id != null ? !id.equals(userIdDto.id) : userIdDto.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
