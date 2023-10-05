package ru.practicum.explore.categories.mapper;

import ru.practicum.explore.categories.dto.CategoryDtoResponse;
import ru.practicum.explore.categories.dto.CategoryDto;
import ru.practicum.explore.categories.model.Category;

public class CategoryMapper {
    public static final CategoryDtoResponse toCategoryDto(Category category) {
        CategoryDtoResponse categoryDto = new CategoryDtoResponse();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static final Category toCategory(CategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }
}
