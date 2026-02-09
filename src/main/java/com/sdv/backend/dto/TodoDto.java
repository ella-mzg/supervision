package com.sdv.backend.dto;

public record TodoDto(
        Long id,
        String title,
        boolean done
) {}
