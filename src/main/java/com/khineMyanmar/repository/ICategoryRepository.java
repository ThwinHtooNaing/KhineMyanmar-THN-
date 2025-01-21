package com.khineMyanmar.repository;

import com.khineMyanmar.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

interface ICategoryRepository extends JpaRepository<Category,Long> {
        
}
