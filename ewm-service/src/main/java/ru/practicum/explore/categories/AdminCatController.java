package ru.practicum.explore.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.categories.dto.CategoryDtoResponse;
import ru.practicum.explore.categories.dto.CategoryDto;
import ru.practicum.explore.categories.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCatController {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDtoResponse create(@Valid @RequestBody CategoryDto categoryDto) {
        return service.create(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDtoResponse update(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Integer catId) {
        return service.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer catId) {
        service.deleteCategory(catId);
    }
}
