package ru.otus.web_service.mapper;

import org.springframework.stereotype.Service;
import ru.otus.web_service.dto.ItemDto;
import ru.otus.web_service.dto.ItemDtos;
import ru.otus.web_service.model.Item;

import java.util.Collection;

@Service
public class ItemMapper {

    public ItemDto itemTo(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .build();
    }

    public ItemDtos listItemsTo(Collection<Item> items) {
        var dtos = items.stream()
                .map(this::itemTo)
                .toList();
        return new ItemDtos(dtos);
    }

    public Item toDomainObject(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId().isPresent() ? itemDto.getId().get() : null)
                .name(itemDto.getName())
                .description(itemDto.getDescription().isPresent() ? itemDto.getDescription().get() : null)
                .price(itemDto.getPrice())
                .build();
    }
}
