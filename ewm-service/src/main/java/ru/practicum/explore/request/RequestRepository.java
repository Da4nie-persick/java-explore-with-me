package ru.practicum.explore.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.events.model.Event;
import ru.practicum.explore.request.enums.RequestStatus;
import ru.practicum.explore.request.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByRequesterId(Integer userId);

    Optional<Request> findByRequesterIdAndEventId(Integer userId, Integer eventId);

    List<Request> findAllByEventInitiatorIdAndEventId(Integer initiatorId, Integer eventId);

    List<Request> findAllByIdInAndStatus(List<Integer> requestIds, RequestStatus status);

    Integer countConfirmedByEventId(Integer eventId);

    Integer countByEventAndStatus(Event event, RequestStatus requestStatus);
}
