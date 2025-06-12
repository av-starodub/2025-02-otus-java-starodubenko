package ru.otus.web_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import ru.otus.web_service.dto.ErrorDto;
import ru.otus.web_service.dto.ItemDto;
import ru.otus.web_service.dto.ItemDtos;
import ru.otus.web_service.exception.ItemNotFoundException;
import ru.otus.web_service.mapper.ItemMapper;
import ru.otus.web_service.model.Item;
import ru.otus.web_service.service.ItemService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody @Valid ItemDto itemDto) {
        var savedItem = itemService.create(itemMapper.toDomainObject(itemDto));
        System.out.println(savedItem.toString());
        var savedItemDto = itemMapper.itemTo(savedItem);
        System.out.println(savedItemDto.toString());
        return new ResponseEntity<>(savedItemDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ItemDtos> getAllItems() {
        var items = itemService.getAll();
        var itemDtos = itemMapper.listItemsTo(items);
        return ResponseEntity.ok(itemDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id) {
        var optionalItem = itemService.getById(id);
        return getItemDtoResponseEntity(optionalItem, id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody @Valid ItemDto itemDto) {
        var optionalItem = itemService.update(id, itemMapper.toDomainObject(itemDto));
        return getItemDtoResponseEntity(optionalItem, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        var isDeleted = itemService.deleteById(id);
        if (!isDeleted) {
            throw new ItemNotFoundException("Item with id=%s not found".formatted(id));
        }
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<ItemDto> getItemDtoResponseEntity(Optional<Item> item, Long id) {
        return item
                .map(itemMapper::itemTo)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ItemNotFoundException("Item with id=%s not found".formatted(id)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException ex) {
        var details = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorDto.of("Input data validation failed", details));
    }
    @ExceptionHandler(ItemNotFoundException.class)
    private ResponseEntity<ErrorDto> handleItemNotFoundException(ItemNotFoundException ex, WebRequest request) {
        var errorDto = ErrorDto.of(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorDto> handleAllExceptions(Exception ex, WebRequest request) {
        var errorDto = ErrorDto.of(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
