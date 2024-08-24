package ru.otus.web_service.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("items")
public class Item {
    @Id
    private Long id;

    private String name;

    private String description;

    private Double price;
}
