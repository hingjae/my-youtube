package com.honey.myyoutube.dto.view;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public static List<CategoryDto> allVideo() {
        return List.of(CategoryDto.of("all", "모든 동영상", null));
    }

    public static List<CategoryDto> initList() {
        return new ArrayList<>(allVideo());
    }
}
