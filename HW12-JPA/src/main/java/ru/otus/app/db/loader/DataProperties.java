package ru.otus.app.db.loader;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DataProperties {

    private final String fileName;

    public static DataProperties create(String fileName) {
        Objects.requireNonNull(fileName, "File name must not be null");
        if (fileName.isEmpty()) {
            throw new IllegalArgumentException("File name must not be empty");
        }
        return new DataProperties(fileName);
    }
}
