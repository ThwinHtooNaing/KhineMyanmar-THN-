package com.khineMyanmar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khineMyanmar.model.Category;
import com.khineMyanmar.repository.ICategoryRepository;

@Service
public class CategoryService {
    
    @Autowired
    private ICategoryRepository categoryRepository;

    public String addCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }

        if (categoryRepository.existsByCategoryName(name)) {
            throw new IllegalStateException("Category already exists");
        }

        Category category = new Category(name);
        categoryRepository.save(category);
        return "Category added successfully";
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
