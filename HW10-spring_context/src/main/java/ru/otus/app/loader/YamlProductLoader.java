package ru.otus.app.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.app.config.ProductProperties;
import ru.otus.app.model.Product;
import ru.otus.app.repository.dto.ProductDto;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

/**
 * Class for loading products from a property.
 */
@Component
public class YamlProductLoader implements ProductLoader {

    private final ProductProperties properties;

    @Autowired
    public YamlProductLoader(ProductProperties prop) {
        Objects.requireNonNull(prop, "Parameter properties must not be null");
        this.properties = prop;
    }

    public List<Product> loadProducts() {
        var resource = new ClassPathResource(properties.getFileName());
        try (var reader = new InputStreamReader(resource.getInputStream())) {
            var objectMapper = new ObjectMapper(new YAMLFactory());
            List<ProductDto> productDtoList = objectMapper.readValue(
                    reader, objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDto.class)
            );
            return productDtoList.stream()
                    .map(ProductDto::toProduct)
                    .toList();
        } catch (IOException e) {
            throw new DataLoadException("Failed to load products from property", e);
        }
    }
}
