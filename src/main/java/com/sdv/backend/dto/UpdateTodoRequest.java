package com.sdv.backend.dto;

public record UpdateTodoRequest(
        String title,
        Boolean done
) {}
