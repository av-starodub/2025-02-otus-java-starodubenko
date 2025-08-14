package ru.otus.app.dto;

import lombok.Data;
import ru.otus.app.model.Product;

import java.math.BigDecimal;

@Data
public class ProductDto implements Dto<Product> {

    private Long id;

    private String name;

    private BigDecimal price;

    @Override
    public Product toDomainObject() {
        return new Product(id, name, price);
    }
}
