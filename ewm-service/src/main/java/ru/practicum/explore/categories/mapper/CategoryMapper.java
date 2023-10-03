package ru.practicum.explore.categories.mapper;

import ru.practicum.explore.categories.dto.CategoryDto;
import ru.practicum.explore.categories.dto.NewCategoryDto;
import ru.practicum.explore.categories.model.Category;
public class CategoryMapper {
    public static final CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static final Category toCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }
}
