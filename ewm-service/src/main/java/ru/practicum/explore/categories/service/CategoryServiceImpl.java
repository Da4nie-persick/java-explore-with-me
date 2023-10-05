package ru.practicum.explore.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.categories.CategoryRepository;
import ru.practicum.explore.categories.dto.CategoryDtoResponse;
import ru.practicum.explore.categories.dto.CategoryDto;
import ru.practicum.explore.categories.mapper.CategoryMapper;
import ru.practicum.explore.categories.model.Category;
import ru.practicum.explore.events.EventRepository;
import ru.practicum.explore.exception.ConditionsNotConflictException;
import ru.practicum.explore.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDtoResponse create(CategoryDto categoryDto) {
        Category category;
        try {
            category = repository.save(CategoryMapper.toCategory(categoryDto));
        } catch (DataIntegrityViolationException e) {
            throw new ConditionsNotConflictException("The category with the name=" + categoryDto.getName() + " is already presented");
        }
        return CategoryMapper.toCategoryDto(category);
    }

    @Transactional
    @Override
    public void deleteCategory(Integer catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + catId + " was not found"));
        if (eventRepository.countByCategoryId(catId) > 0) {
            throw new ConditionsNotConflictException("The category is not empty");
        }
        repository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDtoResponse updateCategory(Integer catId, CategoryDto categoryDto) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + catId + " was not found"));
        category.setName(categoryDto.getName());
        try {
            return CategoryMapper.toCategoryDto(repository.saveAndFlush(category));
        } catch (DataIntegrityViolationException e) {
            throw new ConditionsNotConflictException("The category with the name=" + categoryDto.getName() + " is already presented");
        }
    }

    @Override
    public List<CategoryDtoResponse> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Category> list = repository.findAll(pageable);
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDtoResponse getCategoryId(Integer catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + catId + " was not found"));
        return CategoryMapper.toCategoryDto(category);
    }
}