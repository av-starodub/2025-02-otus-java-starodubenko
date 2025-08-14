package ru.otus.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.app.model.Product;

/**
 * DTO for transferring product data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @JsonProperty("title")
    private String title;

    @JsonProperty("price")
    private Double price;

    public Product toProduct() {
        return new Product(null, title, price);
    }
}
