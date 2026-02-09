package com.sdv.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTodoRequest(
        @NotBlank String title
) {}
