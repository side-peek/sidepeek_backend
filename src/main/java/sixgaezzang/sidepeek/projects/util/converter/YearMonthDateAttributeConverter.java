package sixgaezzang.sidepeek.projects.util.converter;

import jakarta.persistence.AttributeConverter;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;

public class YearMonthDateAttributeConverter implements AttributeConverter<YearMonth, java.sql.Date> {

    @Override
    public java.sql.Date convertToDatabaseColumn(YearMonth attribute) {
        if (attribute != null) {
            return java.sql.Date.valueOf(attribute.atDay(1));
        }
        return null;
    }

    @Override
    public YearMonth convertToEntityAttribute(java.sql.Date dbData) {
        if (dbData != null) {
            return YearMonth.from(getLocalDate(dbData));
        }
        return null;
    }

    private LocalDate getLocalDate(Date dbData) {
        return new Date(dbData.getTime()).toLocalDate();
    }

}
