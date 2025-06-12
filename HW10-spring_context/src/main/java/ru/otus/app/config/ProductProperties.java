package ru.otus.app.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class ProductProperties {

    private final String fileName;

    public static ProductProperties create(String fileName) {
        Objects.requireNonNull(fileName, "File name must not be null");
        if (fileName.isEmpty()) {
            throw new IllegalArgumentException("File name must not be empty");
        }
        return new ProductProperties(fileName);
    }
}
