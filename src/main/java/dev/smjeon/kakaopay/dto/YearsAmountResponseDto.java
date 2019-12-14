package dev.smjeon.kakaopay.dto;

import java.time.Year;
import java.util.Map;

public class YearsAmountResponseDto {
    private Year year;
    private Long totalAmount;
    private Map<String, Long> detailAmount;

    public YearsAmountResponseDto(Year year, Long totalAmount, Map<String, Long> detailAmount) {
        this.year = year;
        this.totalAmount = totalAmount;
        this.detailAmount = detailAmount;
    }

    public Year getYear() {
        return year;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public Map<String, Long> getDetailAmount() {
        return detailAmount;
    }

    public Long getValue(String key) {
        return detailAmount.get(key);
    }
}
