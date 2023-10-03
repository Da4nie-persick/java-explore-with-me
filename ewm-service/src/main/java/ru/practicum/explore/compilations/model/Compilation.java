package ru.practicum.explore.compilations.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore.events.model.Event;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
    @Column(name = "title", nullable = false)
    private String title;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "compilations_events", joinColumns = {@JoinColumn(name = "compilation_id")},
    inverseJoinColumns = {@JoinColumn(name = "event_id")})
    private Set<Event> eventSet = new HashSet<>();
}
