package com.khineMyanmar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.khineMyanmar.model.Category;
import com.khineMyanmar.repository.ICategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Electronics");
    }

    @Test
    void testAddCategory_Success() {
        when(categoryRepository.existsByCategoryName("Electronics")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        String result = categoryService.addCategory("Electronics");
        assertEquals("Category added successfully", result);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testAddCategory_EmptyName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> categoryService.addCategory(" "));
        assertEquals("Category name cannot be empty", exception.getMessage());
    }

    @Test
    void testAddCategory_AlreadyExists() {
        when(categoryRepository.existsByCategoryName("Electronics")).thenReturn(true);

        Exception exception = assertThrows(IllegalStateException.class, () -> categoryService.addCategory("Electronics"));
        assertEquals("Category already exists", exception.getMessage());
    }

    @Test
    void testGetAllCategories() {
        List<Category> categories = Arrays.asList(new Category("Electronics"), new Category("Clothing"));
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();
        assertEquals(2, result.size());
    }

    @Test
    void testGetCategoryById_Found() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(1L);
        assertNotNull(result);
        assertEquals("Electronics", result.getCategoryName());
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Category result = categoryService.getCategoryById(1L);
        assertNull(result);
    }

    @Test
    void testSaveCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.saveCategory(category);
        assertNotNull(result);
        assertEquals("Electronics", result.getCategoryName());
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
