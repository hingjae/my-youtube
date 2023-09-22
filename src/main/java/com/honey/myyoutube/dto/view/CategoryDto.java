package com.honey.myyoutube.dto.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class CategoryDto {
    private String id;
    private String title;
    private Long count;

    public CategoryDto(String id, String title, Long count) {
        this.id = id;
        this.title = title;
        this.count = count;
    }

    public static CategoryDto of(String id, String title, Long count) {
        return new CategoryDto(id, title, count);
    }

}
