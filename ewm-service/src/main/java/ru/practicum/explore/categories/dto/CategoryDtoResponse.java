package ru.practicum.explore.categories.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDtoResponse {
    private Integer id;
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
