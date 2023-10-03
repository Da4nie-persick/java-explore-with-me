package ru.practicum.explore.events;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.events.dto.EventFullDto;
import ru.practicum.explore.events.dto.EventShortDto;
import ru.practicum.explore.events.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
@Validated
public class PublicEventController {
    private final EventService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventId(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        return service.getEventId(id, httpServletRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> searchEventsFilter(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) List<Integer> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false) String rangeStart,
                                                  @RequestParam(required = false) String rangeEnd,
                                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam(required = false) String sort,
                                                  @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(required = false, defaultValue = "10") Integer size,
                                                  HttpServletRequest httpServletRequest) {
        return service.searchEventsFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, httpServletRequest);
    }
}
