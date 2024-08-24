package ru.otus.web_service.dao;

import org.springframework.data.repository.CrudRepository;
import ru.otus.web_service.domain.Item;

public interface ItemDao extends CrudRepository<Item, Long> {
}
