package ru.otus.app.db.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NoArgsConstructor;
import ru.otus.app.db.exception.DataLoadException;
import ru.otus.app.db.exception.ResourceNotFoundException;
import ru.otus.app.dto.Dto;
import ru.otus.app.model.AbstractBaseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class DataLoader {

    public static <T extends AbstractBaseEntity, D extends Dto<T>> List<T> load(DataProperties prop, Class<D> cls) {
        Objects.requireNonNull(prop, "DataProperties must not be null");
        Objects.requireNonNull(cls, "Class must not be null");
        try (var reader = new InputStreamReader(getInputStream(prop))) {
            var mapper = new ObjectMapper(new YAMLFactory());
            mapper.registerModule(new JavaTimeModule());
            List<D> dtos = mapper.readValue(reader, mapper.getTypeFactory().constructCollectionType(List.class, cls));
            return dtos.stream()
                    .map(Dto::toDomainObject)
                    .toList();
        } catch (IOException e) {
            throw new DataLoadException("Failed to load data from property %s".formatted(prop), e);
        }
    }

    private static InputStream getInputStream(DataProperties prop) {
        var classLoader = DataLoader.class.getClassLoader();
        var fileName = prop.getFileName();
        var inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new ResourceNotFoundException("Resource %s not found".formatted(fileName));
        }
        return classLoader.getResourceAsStream(fileName);
    }
}
