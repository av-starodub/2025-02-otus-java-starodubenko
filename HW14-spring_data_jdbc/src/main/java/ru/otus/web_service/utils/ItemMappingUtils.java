package ru.otus.web_service.utils;

import ru.otus.web_service.dto.ItemDto;
import ru.otus.web_service.dto.ItemDtos;
import ru.otus.web_service.model.Item;

import java.util.Collection;

public final class ItemMappingUtils {

    private ItemMappingUtils() {
    }

    public static ItemDto itemTo(Item item) {
        return new ItemDto(item.getName(), item.getDescription(), item.getPrice());
    }

    public static ItemDtos listItemsTo(Collection<Item> items) {
        var dtos = items.stream()
                .map(ItemMappingUtils::itemTo)
                .toList();
        return new ItemDtos(dtos);
    }
}
