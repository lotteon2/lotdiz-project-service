package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
