package ru.practicum.explore.events.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.explore.request.enums.RequestStatus;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private RequestStatus status;
}
