package ru.otus.web_service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorDto {

    private final String message;

    private final List<String> details;

    public static ErrorDto of(String message, List<String> details) {
        return new ErrorDto(message, details);
    }

    public static ErrorDto of(String message, String... details) {
        return new ErrorDto(message, List.of(details));
    }
}

