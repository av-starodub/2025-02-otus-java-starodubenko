package ru.otus.web_service.dto;

import lombok.Data;

@Data
public class ErrorDto {

    private final String errorMessage;

    private final String errorDetails;
}

