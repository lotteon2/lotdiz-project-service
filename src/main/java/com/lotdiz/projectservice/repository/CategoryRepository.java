package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  @Query("select c from Category c where c.categoryId = :id")
  Optional<Category> findByCategoryId(Long id);
}
