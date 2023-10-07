package ru.practicum.explore.stats;

import ru.practicum.explore.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class StatsMapper {
    public static final EndpointHitDto toEndpointHit(HttpServletRequest httpServletRequest) {
        EndpointHitDto hit = new EndpointHitDto();
        hit.setApp("ewm-main-service");
        hit.setUri(httpServletRequest.getRequestURI());
        hit.setIp(httpServletRequest.getRemoteAddr());
        hit.setTimestamp(LocalDateTime.now());
        return hit;
    }
}