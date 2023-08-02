package com.honey.myyoutube.dto.searchcondition;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class VideoSearchCondition {
    private LocalDate searchDate;
    private String categoryId;

    private VideoSearchCondition(LocalDate searchDate, String categoryId) {
        this.searchDate = searchDate;
        this.categoryId = categoryId;
    }

    public static VideoSearchCondition of(LocalDate searchDate, String categoryId) {
        return new VideoSearchCondition(searchDate, categoryId);
    }

    public static VideoSearchCondition of(LocalDate searchDate) {
        return new VideoSearchCondition(searchDate, null);
    }
}
