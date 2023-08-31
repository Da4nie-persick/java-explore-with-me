package ru.practicum.explore.service;

import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.dto.ViewStatsDto;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    EndpointHitDto save(EndpointHitDto hitDto);

    List<ViewStatsDto> getStatistics(LocalDateTime start, LocalDateTime end,
                                     String[] uris, boolean unique);
}
