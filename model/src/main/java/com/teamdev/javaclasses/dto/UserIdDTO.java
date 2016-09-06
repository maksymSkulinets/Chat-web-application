package com.teamdev.javaclasses.dto;

public class UserIdDTO {
    private Long id;

    public UserIdDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserIdDTO userIdDTO = (UserIdDTO) o;

        return id != null ? id.equals(userIdDTO.id) : userIdDTO.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
