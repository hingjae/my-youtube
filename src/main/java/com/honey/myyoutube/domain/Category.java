package com.honey.myyoutube.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@ToString(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Category {

    @Id
    private String id;
    @Column(nullable = false) private String title;

    @Builder
    private Category(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
