package ru.practicum.explore.events;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.events.model.Event;
import ru.practicum.explore.events.model.State;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Integer countByCategoryId(Integer id);

    Set<Event> findAllByIdIn(List<Integer> eventId);

    Optional<Event> findEventByIdAndInitiatorId(Integer id, Integer initiatorId);

    List<Event> findAllByInitiatorId(Integer initiatorId, Pageable pageable);

    @Query(value = "select e from Event e where :text is null " +
            "or lower(e.annotation) like concat('%', :text, '%') " +
            "or lower(e.description) like concat('%', :text, '%') " +
            "and :categories is null or e.category.id in :categories " +
            "and :paid is null or e.paid = :paid " +
            "and e.eventDate >= :rangeStart " +
            "and e.eventDate <= :rangeEnd")
    List<Event> searchByParam(@Param("text") String text,
                              @Param("categories") Collection<Integer> categories,
                              @Param("paid") Boolean paid,
                              @Param("rangeStart") LocalDateTime rangeStart,
                              @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "select e from Event e where " +
            ":users is null or e.initiator.id in :users " +
            "and e.state in :states " +
            "and :categories is null or e.category.id in :categories " +
            "and e.eventDate >= :rangeStart " +
            "and e.eventDate <= :rangeEnd")
    List<Event> findEventsByParam(@Param("users") Collection<Integer> users,
                                  @Param("states") Collection<State> states,
                                  @Param("categories") Collection<Integer> categories,
                                  @Param("rangeStart") LocalDateTime rangeStart,
                                  @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);
}