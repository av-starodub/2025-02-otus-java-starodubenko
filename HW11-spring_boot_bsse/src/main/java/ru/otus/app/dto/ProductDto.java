package ru.otus.app.dto;

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

    private String title;

    private Double price;

    public Product toProduct() {
        return new Product(null, title, price);
    }
}
