package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.StatsRepository;
import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.exception.ValidationException;
import ru.practicum.explore.mapper.EndpointHitMapper;
import ru.practicum.explore.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    @Transactional
    @Override
    public EndpointHitDto save(EndpointHitDto hitDto) {
        EndpointHit endpointHit = repository.save(EndpointHitMapper.toEndpointHit(hitDto));
        return EndpointHitMapper.toEndpointHitDto(endpointHit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStatsDto> getStatistics(LocalDateTime start, LocalDateTime end,
                                            String[] uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new ValidationException("Invalid time");
        }
        List<ViewStatsDto> list;
        if (uris == null) {
            list = unique ? repository.getAllUniqueStatistics(start, end) : repository.getAllStatistics(start, end);
        } else {
            list = unique ? repository.getUniqueUrisStatistics(start, end, uris) : repository.getUrisStatistics(start, end, uris);
        }
        return list;
    }
}