package ru.practicum.explore.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.events.EventRepository;
import ru.practicum.explore.events.model.Event;
import ru.practicum.explore.events.model.State;
import ru.practicum.explore.exception.ConditionsNotConflictException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.request.RequestRepository;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.request.enums.RequestStatus;
import ru.practicum.explore.request.mapper.RequestMapper;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.user.UserRepository;
import ru.practicum.explore.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public ParticipationRequestDto create(Integer userId, Integer eventId) {
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConditionsNotConflictException("You cannot add a repeat request");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found"));
        if (event.getState() != State.PUBLISHED) {
            throw new ConditionsNotConflictException("You cannot participate in an unpublished event");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConditionsNotConflictException("The initiator of the event cannot add a request to participate in his event");
        }
        if (event.getParticipantLimit() != 0 && (event.getParticipantLimit() - event.getConfirmedRequests()) <= 0) {
            throw new ConditionsNotConflictException("The limit of participation requests has been reached");
        }
        RequestStatus status;
        status = event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED;

        if (event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
        }
        if (status == RequestStatus.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        Request request = RequestMapper.toRequest(event, user, status);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getRequest(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User with id=" + userId + " was not found");
        }
        List<Request> list = requestRepository.findAllByRequesterId(userId);
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Integer userId, Integer requestId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User with id=" + userId + " was not found");
        }
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Request with id=" + requestId + "was not found"));
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }
}
