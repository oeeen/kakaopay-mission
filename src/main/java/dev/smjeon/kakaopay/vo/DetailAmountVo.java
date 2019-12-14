package dev.smjeon.kakaopay.vo;

import java.time.Year;

public class DetailAmountVo {
    private Year year;
    private String instituteName;
    private Long amount;

    public DetailAmountVo(Year year, String instituteName, Long amount) {
        this.year = year;
        this.instituteName = instituteName;
        this.amount = amount;
    }

    public Year getYear() {
        return year;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public Long getAmount() {
        return amount;
    }
}
