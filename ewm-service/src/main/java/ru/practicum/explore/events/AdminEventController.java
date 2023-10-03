package ru.practicum.explore.events;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.events.dto.EventFullDto;
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
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> searchEvents(@RequestParam(required = false) List<Integer> users,
                                           @RequestParam(required = false) List<String> states,
                                           @RequestParam(required = false) List<Integer> categories,
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                           @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return service.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateByAdmin(@PathVariable Integer eventId, @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return service.updateByAdmin(eventId, updateEventAdminRequest);
    }
}
