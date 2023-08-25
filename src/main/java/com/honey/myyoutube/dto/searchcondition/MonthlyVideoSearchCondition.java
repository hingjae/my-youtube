package com.honey.myyoutube.dto.searchcondition;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MonthlyVideoSearchCondition {
    private final LocalDate startOfMonth;
    private final LocalDate endOfMonth;
    private final String categoryId;

    private static final int START_OF_DAY = 1;
    private static final int END_OF_DAY = 31;

    @Builder
    public MonthlyVideoSearchCondition(String yearMonth, String categoryId) {
        String[] yearAndMonthString = yearMonth.split("-");
        int[] yearAndMonth = new int[yearAndMonthString.length];

        for (int i = 0; i < yearAndMonthString.length; i++) {
            yearAndMonth[i] = Integer.parseInt(yearAndMonthString[i]);
        }

        this.startOfMonth = LocalDate.of(yearAndMonth[0], yearAndMonth[1], START_OF_DAY);
        this.endOfMonth = LocalDate.of(yearAndMonth[0], yearAndMonth[1], END_OF_DAY);
        this.categoryId = categoryId;
    }
}
