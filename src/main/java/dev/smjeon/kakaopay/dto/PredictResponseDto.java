package dev.smjeon.kakaopay.dto;

import dev.smjeon.kakaopay.vo.Amount;

import java.time.Month;
import java.time.Year;

public class PredictResponseDto {
    private String instituteName;
    private Year year;
    private Month month;
    private Amount amount;

    public PredictResponseDto(String instituteName, Year year, Month month, Amount amount) {
        this.instituteName = instituteName;
        this.year = year;
        this.month = month;
        this.amount = amount;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public Year getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public Amount getAmount() {
        return amount;
    }
}
