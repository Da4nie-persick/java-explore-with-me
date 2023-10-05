package ru.practicum.explore.categories.service;

import ru.practicum.explore.categories.dto.CategoryDtoResponse;
import ru.practicum.explore.categories.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDtoResponse create(CategoryDto newCategoryDto);

    void deleteCategory(Integer catId);

    CategoryDtoResponse updateCategory(Integer catId, CategoryDto newCategoryDto);

    List<CategoryDtoResponse> getCategories(Integer from, Integer size);

    CategoryDtoResponse getCategoryId(Integer catId);
}
