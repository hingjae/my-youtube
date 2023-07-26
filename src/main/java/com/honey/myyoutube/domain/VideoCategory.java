package com.honey.myyoutube.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@ToString(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class VideoCategory {

    @Id private String id;
    @Column(nullable = false) private String title;

    @Builder
    private VideoCategory(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
