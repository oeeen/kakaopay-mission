package dev.smjeon.kakaopay.dto;

import dev.smjeon.kakaopay.vo.MinMaxVo;

public class MinMaxResponseDto {
    private MinMaxVo minimum;
    private MinMaxVo maximum;

    public MinMaxResponseDto(MinMaxVo minimum, MinMaxVo maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public MinMaxVo getMinimum() {
        return minimum;
    }

    public MinMaxVo getMaximum() {
        return maximum;
    }
}
