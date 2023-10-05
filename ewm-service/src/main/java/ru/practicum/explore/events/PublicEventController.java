package ru.practicum.explore.events;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.events.dto.EventFullDto;
import ru.practicum.explore.events.dto.EventShortDto;
import ru.practicum.explore.events.dto.SearchParametersPublic;
import ru.practicum.explore.events.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    public EventFullDto getEventId(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        return service.getEventId(id, httpServletRequest);
    }

    @GetMapping
    public List<EventShortDto> searchEventsFilter(@Valid @ModelAttribute SearchParametersPublic searchParametersPublic,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(defaultValue = "10") Integer size,
                                                  HttpServletRequest httpServletRequest) {
        return service.searchEventsFilter(searchParametersPublic, from, size, httpServletRequest);
    }
}
