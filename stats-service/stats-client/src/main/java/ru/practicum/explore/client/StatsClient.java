package ru.practicum.explore.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;

import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatsClient extends BaseClient {
    private final String applicationName;
    private final String serverUrl;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl,
                       @Value("${spring.application.name}") String applicationName,
                       RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        this.applicationName = applicationName;
        this.serverUrl = serverUrl;
    }

    public ResponseEntity<Object> save(EndpointHitDto endpointHitDto) {
        return post("/hit", endpointHitDto);
    }

    public ResponseEntity<Object> getStatistics(String start, String end,
                                                List<String> uris, Boolean unique) {
            Map<String, Object> parameters = Map.of(
                    "start", start,
                    "end", end,
                    "uris", uris,
                    "unique", unique.toString());
            return get("/" + applicationName + "/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public List<ViewStatsDto> getHits(List<String> uris) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(serverUrl + "/stats")
                .queryParam("start", "1900-01-01 00:00:00")
                .queryParam("end", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .queryParam("uris", StringUtils.join(uris, ','))
                .queryParam("unique", "true").build();
        ViewStatsDto[] stats = rest.getForObject(uriComponents.toString(), ViewStatsDto[].class);
        return Arrays.stream(stats).collect(Collectors.toUnmodifiableList());
    }
}