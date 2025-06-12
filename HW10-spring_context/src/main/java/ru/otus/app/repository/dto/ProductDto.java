package ru.otus.app.repository.dto;

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

    private String name;

    private Double price;

    public Product toProduct() {
        return new Product(null, name, price);
    }
}
