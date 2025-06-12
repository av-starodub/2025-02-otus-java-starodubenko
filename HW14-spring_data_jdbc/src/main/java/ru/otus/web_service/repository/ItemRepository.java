package ru.otus.web_service.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.web_service.model.Item;

public interface ItemRepository extends CrudRepository<Item, Long> {
}
