package dev.smjeon.kakaopay.domain;

public class InstituteResponseDto {
    private String name;
    private String code;

    public InstituteResponseDto(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
