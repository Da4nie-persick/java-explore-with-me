package ru.practicum.explore.events;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.events.dto.EventFullDto;
import ru.practicum.explore.events.dto.SearchParametersAdmin;
import ru.practicum.explore.events.dto.UpdateEventAdminRequest;
import ru.practicum.explore.events.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@Validated
public class AdminEventController {
    private final EventService service;

    @GetMapping
    public List<EventFullDto> searchEvents(@Valid @ModelAttribute SearchParametersAdmin searchParametersAdmin,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @Positive @RequestParam(defaultValue = "10") Integer size) {
        return service.searchEvents(searchParametersAdmin, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateByAdmin(@PathVariable Integer eventId, @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return service.updateByAdmin(eventId, updateEventAdminRequest);
    }
}
