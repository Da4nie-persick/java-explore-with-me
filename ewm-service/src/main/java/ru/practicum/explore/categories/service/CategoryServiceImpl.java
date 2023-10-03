package ru.practicum.explore.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.categories.CategoryRepository;
import ru.practicum.explore.categories.dto.CategoryDto;
import ru.practicum.explore.categories.dto.NewCategoryDto;
import ru.practicum.explore.categories.mapper.CategoryMapper;
import ru.practicum.explore.categories.model.Category;
import ru.practicum.explore.events.EventRepository;
import ru.practicum.explore.exception.ConditionsNotConflictException;
import ru.practicum.explore.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        if (repository.findByName(newCategoryDto.getName()).isPresent()) {
            throw new ConditionsNotConflictException("The category with the name=" + newCategoryDto.getName() + " is already presented");
        }
        Category category = repository.save(CategoryMapper.toCategory(newCategoryDto));
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
    public CategoryDto updateCategory(Integer catId, NewCategoryDto newCategoryDto) {
        Optional<Category> optionalCategory = repository.findByName(newCategoryDto.getName());
        if (optionalCategory.isPresent()) {
            if (optionalCategory.get().getId().equals(catId)) {
                throw new ConditionsNotConflictException("The category with the name=" + newCategoryDto.getName() + " is already presented");
            }
        }
        Category category = repository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + catId + " was not found"));
        category.setName(newCategoryDto.getName());
        repository.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
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
    public CategoryDto getCategoryId(Integer catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + catId + " was not found"));
        return CategoryMapper.toCategoryDto(category);
    }
}