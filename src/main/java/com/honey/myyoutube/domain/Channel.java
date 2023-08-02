package com.honey.myyoutube.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@ToString(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Channel {
    @Id
    private String id;
    private String title;

    @Builder
    private Channel(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
