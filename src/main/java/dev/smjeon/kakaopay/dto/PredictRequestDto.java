package dev.smjeon.kakaopay.dto;

import java.time.Month;

public class PredictRequestDto {
    private String instituteName;
    private Month month;

    public PredictRequestDto(String instituteName, Month month) {
        this.instituteName = instituteName;
        this.month = month;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public Month getMonth() {
        return month;
    }
}
