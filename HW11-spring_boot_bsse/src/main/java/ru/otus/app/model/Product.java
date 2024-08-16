package ru.otus.app.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Product {

    private Long id;

    private String title;

    private double price;
}
