package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.model.EndpointHit;

public class EndpointHitMapper {
    public static final EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit hit = new EndpointHit();
        hit.setId(endpointHitDto.getId());
        hit.setApp(endpointHitDto.getApp());
        hit.setUri(endpointHitDto.getUri());
        hit.setIp(endpointHitDto.getIp());
        hit.setTimestamp(endpointHitDto.getTimestamp());
        return hit;
    }

    public static final EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        EndpointHitDto hitDto = new EndpointHitDto();
        hitDto.setId(endpointHit.getId());
        hitDto.setApp(endpointHit.getApp());
        hitDto.setUri(endpointHit.getUri());
        hitDto.setIp(endpointHit.getIp());
        hitDto.setTimestamp(endpointHit.getTimestamp());
        return hitDto;
    }
}
