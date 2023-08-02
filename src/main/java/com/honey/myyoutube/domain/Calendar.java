package com.honey.myyoutube.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Calendar {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate calendarDate;
    private LocalDateTime calendarDateTime;

    @Builder
    private Calendar(Long id, LocalDate calendarDate, LocalDateTime calendarDateTime) {
        this.id = id;
        this.calendarDate = calendarDate;
        this.calendarDateTime = calendarDateTime;
    }
}
