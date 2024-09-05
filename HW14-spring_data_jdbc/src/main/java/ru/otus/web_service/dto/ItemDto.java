package ru.otus.web_service.dto;

import lombok.Data;
import ru.otus.web_service.model.Item;

@Data
public class ItemDto {
    private final Long id;

    private final String name;

    private final String description;

    private final Double price;

    public Item toDomainObject() {
        return new Item(id, name, description, price);
    }
}
