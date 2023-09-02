package com.honey.myyoutube.dto.searchcondition;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MonthlyVideoSearchCondition {
    private final Integer year;
    private final Integer month;
    private final String categoryId;

    @Builder
    public MonthlyVideoSearchCondition(String yearMonth, String categoryId) {
        String[] yearAndMonthString = yearMonth.split("-");
        int[] yearAndMonth = new int[yearAndMonthString.length];

        for (int i = 0; i < yearAndMonthString.length; i++) {
            yearAndMonth[i] = Integer.parseInt(yearAndMonthString[i]);
        }
        this.year = yearAndMonth[0];
        this.month = yearAndMonth[1];
        this.categoryId = categoryId;
    }
}
