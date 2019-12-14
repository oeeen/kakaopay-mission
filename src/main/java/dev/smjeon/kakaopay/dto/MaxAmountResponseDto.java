package dev.smjeon.kakaopay.dto;

import java.time.Year;

public class MaxAmountResponseDto {
    private Year year;
    private String instituteName;

    public MaxAmountResponseDto(Year year, String instituteName) {
        this.year = year;
        this.instituteName = instituteName;
    }

    public Year getYear() {
        return year;
    }

    public String getInstituteName() {
        return instituteName;
    }
}