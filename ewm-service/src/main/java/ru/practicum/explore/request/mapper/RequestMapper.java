package ru.practicum.explore.request.mapper;

import ru.practicum.explore.events.model.Event;
import ru.practicum.explore.request.enums.RequestStatus;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.user.model.User;

import java.time.LocalDateTime;

public class RequestMapper {
    public static final ParticipationRequestDto toParticipationRequestDto(Request request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(request.getId());
        participationRequestDto.setCreated(request.getCreated());
        participationRequestDto.setEvent(request.getEvent().getId());
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setStatus(request.getStatus());
        return participationRequestDto;
    }

    public static final Request toRequest(Event event, User user, RequestStatus status) {
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);
        request.setStatus(status);
        return request;
    }
}
