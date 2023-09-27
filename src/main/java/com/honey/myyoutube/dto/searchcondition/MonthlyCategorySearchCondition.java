package com.honey.myyoutube.dto.searchcondition;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MonthlyCategorySearchCondition {
    private int year;
    private int month;

    @Builder
    private MonthlyCategorySearchCondition(int year, int month) {
        this.year = year;
        this.month = month;
    }
}
