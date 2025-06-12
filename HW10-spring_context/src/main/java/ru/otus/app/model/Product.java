package ru.otus.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    private int id;

    private String name;

    private double price;

    @Override
    public String toString() {
        return "Product{id=%d, name='%s', price=%f}".formatted(id, name, price);
    }
}
