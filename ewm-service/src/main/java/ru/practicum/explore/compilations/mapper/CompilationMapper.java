package ru.practicum.explore.compilations.mapper;

import ru.practicum.explore.compilations.dto.CompilationDto;
import ru.practicum.explore.compilations.dto.NewCompilationDto;
import ru.practicum.explore.compilations.model.Compilation;
import ru.practicum.explore.events.mapper.EventMapper;
import ru.practicum.explore.events.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static final Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> eventSet) {
        Compilation compilation = new Compilation();
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setEventSet(eventSet);
        return compilation;
    }

    public static final CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setEvents(compilation.getEventSet().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList()));
        return compilationDto;
    }
}
