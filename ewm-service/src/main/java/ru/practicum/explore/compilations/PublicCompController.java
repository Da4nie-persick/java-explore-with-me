package ru.practicum.explore.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilations.dto.CompilationDto;
import ru.practicum.explore.compilations.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
@Validated
public class PublicCompController {
    private final CompilationService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                @Positive @RequestParam(required = false, defaultValue = "10")Integer size) {
        return service.getCompilations(pinned, from, size);
    }

    @GetMapping("{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationId(@PathVariable Integer compId) {
        return service.getCompilationId(compId);
    }
}
