package dev.smjeon.kakaopay.dto;

public class UserResponseDto {
    private Long id;
    private String userId;
    private String token;

    public UserResponseDto(Long id, String userId, String token) {
        this.id = id;
        this.userId = userId;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}
