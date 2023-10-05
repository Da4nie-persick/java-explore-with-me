package ru.practicum.explore.events.service;

import ru.practicum.explore.events.dto.*;
import ru.practicum.explore.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    EventFullDto create(Integer userId, NewEventDto newEventDto);

    EventFullDto updateUserEvent(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest);

    EventRequestStatusUpdateResult updateStatus(Integer userId, Integer eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<EventShortDto> getEvent(Integer userId, Integer from, Integer size);

    EventFullDto getEventFull(Integer userId, Integer eventId);

    List<ParticipationRequestDto> getUserEventRequests(Integer userId, Integer eventId);

    List<EventFullDto> searchEvents(SearchParametersAdmin param, Integer from, Integer size);

    EventFullDto updateByAdmin(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> searchEventsFilter(SearchParametersPublic param, Integer from, Integer size,
                                           HttpServletRequest httpServletRequest);

    EventFullDto getEventId(Integer id, HttpServletRequest httpServletRequest);
}
