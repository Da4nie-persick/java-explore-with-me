package ru.practicum.explore.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.client.StatsClient;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.events.model.Event;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssemblingViews {
    private final StatsClient statsClient;

    public void getViews(List<Event> events) {
        List<String> uris = new ArrayList<>();
        for (Event event : events) {
            uris.add("/events/" + event.getId());
        }
        Map<Integer, Long> views = statsClient.getHits(uris).stream().filter(s -> s.getApp().equals("ewm-main-service"))
                .collect(Collectors.toMap(v -> Integer.parseInt(v.getUri().substring(8)), ViewStatsDto::getHits));
        events.forEach(e -> e.setViews(views.get(e.getId())));
    }
}