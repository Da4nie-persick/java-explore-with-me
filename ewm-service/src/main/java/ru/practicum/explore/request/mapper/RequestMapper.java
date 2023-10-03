package ru.practicum.explore.request.mapper;

import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.request.dto.ParticipationRequestDto;

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
}
