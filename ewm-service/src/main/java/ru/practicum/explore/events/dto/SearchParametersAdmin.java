package ru.practicum.explore.events.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SearchParametersAdmin {
    private List<Integer> users;
    private List<String> states;
    private List<Integer> categories;
    private String rangeStart;
    private String rangeEnd;
}
