package ru.otus.web_service.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ItemDtos {
    private final List<ItemDto> itemDtos;
}
