package com.honey.myyoutube.dto.score;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class VideoScore {
    private String videoId;
    private double score;

    public VideoScore(String videoId, double score) {
        this.videoId = videoId;
        this.score = score;
    }
}
