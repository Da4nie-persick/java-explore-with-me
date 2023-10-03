package ru.practicum.explore.request.service;

import ru.practicum.explore.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto create(Integer userId, Integer eventId);

    List<ParticipationRequestDto> getRequest(Integer userId);

    ParticipationRequestDto cancelRequest(Integer userId, Integer requestId);
}
