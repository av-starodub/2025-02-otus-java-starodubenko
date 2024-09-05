package ru.otus.web_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table("items")
public class Item {
    @Id
    private Long id;

    private String name;

    private String description;

    private Double price;
}
