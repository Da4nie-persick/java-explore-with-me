package ru.practicum.explore.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.categories.CategoryRepository;
import ru.practicum.explore.categories.model.Category;
import ru.practicum.explore.client.StatsClient;
import ru.practicum.explore.events.EventRepository;
import ru.practicum.explore.events.dto.*;
import ru.practicum.explore.events.enums.StateAction;
import ru.practicum.explore.events.enums.StateSort;
import ru.practicum.explore.events.mapper.EventMapper;
import ru.practicum.explore.events.model.Event;
import ru.practicum.explore.events.model.State;
import ru.practicum.explore.exception.ConditionsNotConflictException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.exception.ValidationException;
import ru.practicum.explore.request.RequestRepository;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.request.enums.RequestStatus;
import ru.practicum.explore.request.mapper.RequestMapper;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.stats.AssemblingViews;
import ru.practicum.explore.stats.StatsMapper;
import ru.practicum.explore.user.UserRepository;
import ru.practicum.explore.user.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatsClient client;
    private final AssemblingViews assemblingViews;

    /*
    private
     */
    @Transactional
    @Override
    public EventFullDto create(Integer userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate() != null && newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + newEventDto.getEventDate());
        }
        User initiator = userRepository.findById(userId).
                orElseThrow(() -> new ObjectNotFoundException("Field: initiator. Error: must not be blank. Value: null"));
        Category category = categoryRepository.findById(newEventDto.getCategory()).
                orElseThrow(() -> new ObjectNotFoundException("Field: category. Error: must not be blank. Value: null"));
        Event event = EventMapper.toEvent(newEventDto, initiator, category);
        Integer countConfirmed = requestRepository.countConfirmedByEventId(event.getId());
        return EventMapper.toEventFullDto(eventRepository.save(event), countConfirmed);
    }

    @Transactional
    @Override
    public EventFullDto updateUserEvent(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Field: initiator. Error: must not be blank. Value: null");
        }
        if (updateEventUserRequest.getEventDate() != null && updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + updateEventUserRequest.getEventDate());
        }
        Event event = eventRepository.findEventByIdAndInitiatorId(eventId, userId).
                orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + "was not found"));
        Integer countConfirmed = requestRepository.countConfirmedByEventId(event.getId());
        if (event.getState() == State.PUBLISHED) {
            throw new ConditionsNotConflictException("Only pending or canceled events can be changed");
        }
        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null) {
            categoryRepository.findById(updateEventUserRequest.getCategory()).ifPresent(c -> event.setCategory(c));
        }
        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLocation(updateEventUserRequest.getLocation());
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        if (updateEventUserRequest.getStateAction() != null) {
            switch (updateEventUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
            }
        }
        return EventMapper.toEventFullDto(eventRepository.save(event), countConfirmed);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateStatus(Integer userId, Integer eventId, EventRequestStatusUpdateRequest requestStatus) {
        Event event = eventRepository.findEventByIdAndInitiatorId(eventId, userId).
                orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + "was not found"));
        if (requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED) >= event.getParticipantLimit()) {
            throw new ConditionsNotConflictException("The participant limit has been reached");
        }
        for (Integer id : requestStatus.getRequestIds()) {
            Request request = requestRepository.findById(id).
                    orElseThrow(() -> new ObjectNotFoundException("Request with id=" + id + "was not found"));
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConditionsNotConflictException("The status can be changed only for applications that are in the " + RequestStatus.PENDING);
            }
            if (requestStatus.getStatus().equals(RequestStatus.CONFIRMED)) {
                request.setStatus(RequestStatus.CONFIRMED);
            } else if (requestStatus.getStatus().equals(RequestStatus.REJECTED)) {
                request.setStatus(RequestStatus.REJECTED);
            }
        }
        List<ParticipationRequestDto> confirmedList = requestRepository.findAllByIdInAndStatus(requestStatus.getRequestIds(),
                        RequestStatus.CONFIRMED)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());

        List<ParticipationRequestDto> rejectedList = requestRepository.findAllByIdInAndStatus(requestStatus.getRequestIds(),
                        RequestStatus.REJECTED)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(confirmedList);
        result.setRejectedRequests(rejectedList);
        return result;
    }

    @Override
    public List<EventShortDto> getEvent(Integer userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Event> eventList = eventRepository.findAllByInitiatorId(userId, pageable);
        return eventList.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventFull(Integer userId, Integer eventId) {
        Event event = eventRepository.findEventByIdAndInitiatorId(eventId, userId).
                orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + "was not found"));
        Integer countConfirmed = requestRepository.countConfirmedByEventId(event.getId());
        return EventMapper.toEventFullDto(event, countConfirmed);
    }

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(Integer userId, Integer eventId) {
        List<Request> requestList = requestRepository.findAllByEventInitiatorIdAndEventId(userId, eventId);
        if (requestList.isEmpty()) {
            return new ArrayList<>();
        }
        return requestList.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    /*
    admin
     */
    @Override
    public List<EventFullDto> searchEvents(List<Integer> users, List<String> states, List<Integer> categories, String rangeStart,
                                           String rangeEnd, Integer from, Integer size) {
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(from, size);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start;
        LocalDateTime end;
        if (rangeStart == null || rangeEnd == null) {
            start = LocalDateTime.now();
            end = LocalDateTime.of(3000, Month.JANUARY, 01, 0, 0, 0);
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }
        if (end.isBefore(start)) {
            throw new ValidationException("Invalid date");
        }
        List<State> stateList = states == null ? List.of(State.PUBLISHED, State.PENDING, State.CANCELED) :
                states.stream().map(State::valueOf).collect(Collectors.toList());
        if (users == null) {
            users = null;
        }
        if (categories == null) {
            categories = null;
        }
        List<Event> eventList = eventRepository.findEventsByParam(users, stateList, categories, start, end, pageable);

        for (Event event : eventList) {
            Integer countConfirmed = requestRepository.countConfirmedByEventId(event.getId());
            eventFullDtoList.add(EventMapper.toEventFullDto(event, countConfirmed));
        }
        return eventFullDtoList;
    }

    @Transactional
    @Override
    public EventFullDto updateByAdmin(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).
                orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + "was not found"));
        Integer countConfirmed = requestRepository.countConfirmedByEventId(event.getId());
        if (updateEventAdminRequest.getEventDate() != null && updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException("The start date of the event to be modified must be no earlier than an hour from the date of publication");
        }
        if (event.getState() != State.PENDING && updateEventAdminRequest.getStateAction() == StateAction.PUBLISH_EVENT) {
            throw new ConditionsNotConflictException("Cannot publish the event because it's not in the right state: " + event.getState());
        }
        if (event.getState() == State.PUBLISHED && updateEventAdminRequest.getStateAction() == StateAction.REJECT_EVENT) {
            throw new ConditionsNotConflictException("Cannot publish the event because it's not in the right state: " + event.getState());
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            categoryRepository.findById(updateEventAdminRequest.getCategory()).ifPresent(c -> event.setCategory(c));
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(updateEventAdminRequest.getLocation());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            switch (updateEventAdminRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(State.PUBLISHED);
                    break;
                case REJECT_EVENT:
                    event.setState(State.CANCELED);
                    break;
            }
        }
        return EventMapper.toEventFullDto(eventRepository.save(event), countConfirmed);
    }

    /*
    public
     */
    @Override
    public List<EventShortDto> searchEventsFilter(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                                  String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size,
                                                  HttpServletRequest httpServletRequest) {
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "eventDate"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = rangeStart == null ? LocalDateTime.now() : LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = rangeEnd == null ? LocalDateTime.now().plusYears(90) : LocalDateTime.parse(rangeEnd, formatter);
        if (end.isBefore(start)) {
            throw new ValidationException("Invalid date");
        }
        if (text != null) {
            text = text.toLowerCase();
        } else {
            text = null;
        }
        if (categories == null) {
            categories = null;
        }
        if (paid == null) {
            paid = null;
        }
        List<Event> eventList = eventRepository.searchByParam(text, categories, paid, start, end, pageable);

        if (onlyAvailable) {
            eventList = eventList.stream().filter(e -> e.getConfirmedRequests() < e.getParticipantLimit()).collect(Collectors.toList());
        }
        if (sort != null && sort.equals(StateSort.VIEWS.toString())) {
            eventList.sort((e1, e2) -> e2.getViews().compareTo(e1.getViews()));
        }
        client.save(StatsMapper.toEndpointHit(httpServletRequest));
        assemblingViews.getViews(eventList);
        return eventList.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventId(Integer id, HttpServletRequest httpServletRequest) {
        Event event = eventRepository.findById(id).
                orElseThrow(() -> new ObjectNotFoundException("Event with id=" + id + "was not found"));
        Integer countConfirmed = requestRepository.countConfirmedByEventId(event.getId());
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ObjectNotFoundException("Event with id=" + id + "was not found");
        }
        client.save(StatsMapper.toEndpointHit(httpServletRequest));
        assemblingViews.getViews(List.of(event));
        return EventMapper.toEventFullDto(event, countConfirmed);
    }
}