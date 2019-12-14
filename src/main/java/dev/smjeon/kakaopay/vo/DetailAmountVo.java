package dev.smjeon.kakaopay.vo;

import java.time.Year;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailAmountVo that = (DetailAmountVo) o;
        return Objects.equals(year, that.year) &&
                Objects.equals(instituteName, that.instituteName) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, instituteName, amount);
    }
}
