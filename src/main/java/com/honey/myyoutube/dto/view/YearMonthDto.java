package com.honey.myyoutube.dto.view;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@ToString
@Getter
public class YearMonthDto {
    private final Integer year;
    private final Integer month;

    private final String yearMonth;

    @Builder
    public YearMonthDto(String yearMonthParam) {
        String[] yearAndMonth = yearMonthParam.split("-");
        this.year = Integer.parseInt(yearAndMonth[0]);
        this.month = Integer.parseInt(yearAndMonth[1]);
        this.yearMonth = yearMonthParam;
    }

    public YearMonthDto(Integer year, Integer month) {
        this.year = year;
        this.month = month;
        this.yearMonth = year + "-" + month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YearMonthDto that)) return false;
        return Objects.equals(getYear(), that.getYear()) && Objects.equals(getMonth(), that.getMonth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getYear(), getMonth());
    }
}
