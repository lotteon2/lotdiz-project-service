package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.entity.Product;
import com.lotdiz.projectservice.entity.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByProject(Project project);
}
