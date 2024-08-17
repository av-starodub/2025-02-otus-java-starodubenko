package ru.otus.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    private Long id;

    private String title;

    private double price;
}
