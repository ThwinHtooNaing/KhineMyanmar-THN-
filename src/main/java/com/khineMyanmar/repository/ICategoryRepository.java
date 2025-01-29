package com.khineMyanmar.repository;

import com.khineMyanmar.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByCategoryName(String categoryName);
}
