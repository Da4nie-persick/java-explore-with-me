package ru.practicum.explore.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.compilations.CompilationRepository;
import ru.practicum.explore.compilations.dto.CompilationDto;
import ru.practicum.explore.compilations.dto.NewCompilationDto;
import ru.practicum.explore.compilations.dto.UpdateCompilationRequest;
import ru.practicum.explore.compilations.mapper.CompilationMapper;
import ru.practicum.explore.compilations.model.Compilation;
import ru.practicum.explore.events.EventRepository;
import ru.practicum.explore.events.model.Event;
import ru.practicum.explore.exception.ObjectNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Set<Event> eventSet = newCompilationDto.getEvents() != null ? eventRepository.findAllByIdIn(newCompilationDto.getEvents()) :
                Collections.emptySet();
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto, eventSet));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation with id=" + compId + " was not found"));
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEventSet(eventRepository.findAllByIdIn(updateCompilationRequest.getEvents()));
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Transactional
    @Override
    public void deleteCompilation(Integer compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new ObjectNotFoundException("Compilation with id=" + compId + " was not found");
        }
        compilationRepository.deleteById(compId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> list = pinned != null ? compilationRepository.findAllByPinned(pinned, pageable) :
                compilationRepository.findAll(pageable).getContent();
        return list.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilationId(Integer compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation with id=" + compId + " was not found"));
        return CompilationMapper.toCompilationDto(compilation);
    }
}
