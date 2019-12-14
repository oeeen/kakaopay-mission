package dev.smjeon.kakaopay.vo;

import java.time.Year;
import java.util.Objects;

public class MinMaxVo {
    private final Year year;
    private final Long amount;

    public MinMaxVo(final Year year, final Long amount) {
        this.year = year;
        this.amount = amount;
    }

    public Year getYear() {
        return year;
    }

    public Long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinMaxVo minMaxVo = (MinMaxVo) o;
        return Objects.equals(year, minMaxVo.year) &&
                Objects.equals(amount, minMaxVo.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, amount);
    }
}
