package fr.takima.training.simpleapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudentDto(
        @Size(max = 20) String firstname,
        @NotNull @Size(max = 30) String lastname,
        @NotNull Long departmentId
) {}