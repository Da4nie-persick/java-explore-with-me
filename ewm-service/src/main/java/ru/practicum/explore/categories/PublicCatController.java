package ru.practicum.explore.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.categories.dto.CategoryDtoResponse;
import ru.practicum.explore.categories.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
@Validated
public class PublicCatController {
    private final CategoryService service;

    @GetMapping
    public List<CategoryDtoResponse> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10")Integer size) {
        return service.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDtoResponse getCategoryId(@PathVariable Integer catId) {
        return service.getCategoryId(catId);
    }
}
