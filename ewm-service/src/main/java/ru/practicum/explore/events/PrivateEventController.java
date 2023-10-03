package ru.practicum.explore.events;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.events.dto.*;
import ru.practicum.explore.events.service.EventService;
import ru.practicum.explore.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
@Validated
public class PrivateEventController {
    private final EventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Integer userId, @Valid @RequestBody NewEventDto newEventDto) {
        return service.create(userId, newEventDto);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateUserEvent(@PathVariable Integer userId, @PathVariable Integer eventId,
                                        @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return service.updateUserEvent(userId, eventId, updateEventUserRequest);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateStatus(@PathVariable Integer userId, @PathVariable Integer eventId,
                                                       @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return service.updateStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvent(@PathVariable Integer userId,
                                        @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                        @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return service.getEvent(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventFull(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return service.getEventFull(userId,eventId);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUserEventRequests(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return service.getUserEventRequests(userId,eventId);
    }
}
