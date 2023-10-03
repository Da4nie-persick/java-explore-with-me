package ru.practicum.explore.categories.service;

import ru.practicum.explore.categories.dto.CategoryDto;
import ru.practicum.explore.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    void deleteCategory(Integer catId);

    CategoryDto updateCategory(Integer catId, NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryId(Integer catId);
}
