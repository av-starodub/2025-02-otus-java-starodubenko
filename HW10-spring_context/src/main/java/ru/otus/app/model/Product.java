package ru.otus.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long id;

    private String name;

    private double price;

    @Override
    public String toString() {
        return "Product{id=%d, name='%s', price=%f}".formatted(id, name, price);
    }
}
