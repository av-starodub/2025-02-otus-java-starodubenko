package ru.otus.app.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.app.config.ProductProperties;
import ru.otus.app.dto.ProductsDto;
import ru.otus.app.exception.DataLoadException;

import java.io.IOException;
import java.io.InputStreamReader;

import static java.util.Objects.requireNonNull;

@Component
public class JsonProductLoader implements ProductLoader {

    private final ProductProperties properties;

    @Autowired
    public JsonProductLoader(ProductProperties prop) {
        requireNonNull(prop, "ProductProperties must not be null");
        this.properties = prop;
    }

    @Override
    public ProductsDto loadProducts() {
        var resource = new ClassPathResource(properties.getFileName());
        try (var reader = new InputStreamReader(resource.getInputStream())) {
            var objectMapper = new ObjectMapper();
            return objectMapper.readValue(reader, ProductsDto.class);
        } catch (IOException e) {
            throw new DataLoadException("Failed to load products from JSON", e);
        }
    }
}
