package dev.smjeon.kakaopay.dto;

public class UserRequestDto {
    private String userId;
    private String userPassword;

    public UserRequestDto(String userId, String userPassword) {
        this.userId = userId;
        this.userPassword = userPassword;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
