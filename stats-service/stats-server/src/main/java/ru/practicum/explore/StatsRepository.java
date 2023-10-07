package ru.practicum.explore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {
    @Query(value = "select new ru.practicum.explore.dto.ViewStatsDto(e.app, e.uri, count(e.ip)) from EndpointHit e " +
            "where e.timestamp between :start and :end " +
            "group by e.app, e.uri order by count(e.ip) desc")
    List<ViewStatsDto> getAllStatistics(LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.explore.dto.ViewStatsDto(e.app, e.uri, count(distinct e.ip)) from EndpointHit e " +
            "where e.timestamp between :start and :end " +
            "group by e.app, e.uri order by count(distinct e.ip) desc")
    List<ViewStatsDto> getAllUniqueStatistics(LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.explore.dto.ViewStatsDto(e.app, e.uri, count(e.ip)) from EndpointHit e " +
            "where e.timestamp between :start and :end and e.uri in :uris " +
            "group by e.app, e.uri order by count(e.ip) desc")
    List<ViewStatsDto> getUrisStatistics(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query(value = "select new ru.practicum.explore.dto.ViewStatsDto(e.app, e.uri, count(distinct e.ip)) from EndpointHit e " +
            "where e.timestamp between :start and :end " +
            "and e.uri in :uris group by e.app, e.uri order by count(distinct e.ip) desc")
    List<ViewStatsDto> getUniqueUrisStatistics(LocalDateTime start, LocalDateTime end, String[] uris);
}