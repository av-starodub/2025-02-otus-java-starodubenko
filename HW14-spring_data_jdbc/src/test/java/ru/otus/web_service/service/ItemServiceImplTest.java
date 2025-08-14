package ru.otus.web_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.web_service.model.Item;
import ru.otus.web_service.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    private static final Item ITEM_1 = new Item(1L, "first", "desc", 10.0);

    private static final Item ITEM_2 = new Item(1L, "second", "desc", 20.0);

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    @DisplayName("Should return all items when items exist")
    void checkGetAllItems() {
        when(itemRepository.findAll()).thenReturn(List.of(ITEM_1, ITEM_2));

        var items = itemService.getAll();

        assertThat(items)
                .hasSize(2)
                .containsOnly(ITEM_1, ITEM_2);
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return item when item is found by ID")
    void checkGetItemByIdFound() {
        var itemId = ITEM_1.getId();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(ITEM_1));

        var optionalItem = itemService.getById(itemId);

        assertThat(optionalItem)
                .isPresent()
                .get()
                .isEqualTo(ITEM_1);

        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    @DisplayName("Should return empty when item is not found by ID")
    void checkGetItemByIdNotFound() {
        var itemId = ITEM_1.getId();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        var optionalItem = itemService.getById(itemId);

        assertThat(optionalItem).isNotPresent();

        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    @DisplayName("Should create and return a new item")
    void checkCreateItem() {
        when(itemRepository.save(any())).thenReturn(ITEM_1);

        var createdItem = itemService.create(ITEM_1);

        assertThat(createdItem)
                .isNotNull()
                .isEqualTo(ITEM_1);

        verify(itemRepository, times(1)).save(ITEM_1);
    }

    @Test
    @DisplayName("Should delete item when ID exists")
    void checkDeleteItemById() {
        var itemId = ITEM_1.getId();

        when(itemRepository.existsById(itemId)).thenReturn(true);
        doNothing().when(itemRepository).deleteById(anyLong());

        var isItemDeleted = itemService.deleteById(itemId);

        assertThat(isItemDeleted).isTrue();

        verify(itemRepository, times(1)).deleteById(itemId);
    }
}
