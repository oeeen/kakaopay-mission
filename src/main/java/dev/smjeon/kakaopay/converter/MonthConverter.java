package dev.smjeon.kakaopay.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Month;

@Converter(autoApply = true)
public class MonthConverter implements AttributeConverter<Month, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Month attribute) {
        return attribute.getValue();
    }

    @Override
    public Month convertToEntityAttribute(Integer dbData) {
        return Month.of(dbData);
    }
}
