package dev.smjeon.kakaopay.dto;

public class UserResponseDto {
    private Long id;
    private String userId;

    public UserResponseDto(Long id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }
}
