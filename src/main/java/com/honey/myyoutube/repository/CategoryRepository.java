package com.honey.myyoutube.repository;

import com.honey.myyoutube.domain.Category;
import com.honey.myyoutube.repository.custom.CategorySearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String>, CategorySearchRepository {
}
