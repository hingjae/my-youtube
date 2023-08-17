package com.honey.myyoutube.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@ToString(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Calendar {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate calendarDate;

    @Builder
    private Calendar(Long id, LocalDate calendarDate) {
        this.id = id;
        this.calendarDate = calendarDate;
    }

    public static Calendar of(LocalDate calendarDate) {
        return Calendar.builder()
                .calendarDate(calendarDate)
                .build();
    }
}
