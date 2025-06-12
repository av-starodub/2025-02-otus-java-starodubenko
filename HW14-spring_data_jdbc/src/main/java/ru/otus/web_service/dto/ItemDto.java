package ru.otus.web_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@Getter
@Setter
@Builder
@ToString
public class ItemDto {

    private final Long id;

    @NotBlank(message = "Name is required")
    private final String name;

    private final String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private final Double price;

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }
}
