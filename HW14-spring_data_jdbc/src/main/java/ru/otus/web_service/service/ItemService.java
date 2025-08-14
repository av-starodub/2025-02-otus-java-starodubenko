package ru.otus.web_service.service;

import org.springframework.lang.NonNull;
import ru.otus.web_service.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item create(@NonNull Item item);

    Optional<Item> getById(@NonNull Long id);

    List<Item> getAll();

    Optional<Item> update(@NonNull Long id, @NonNull Item item);

    boolean deleteById(@NonNull Long id);
}