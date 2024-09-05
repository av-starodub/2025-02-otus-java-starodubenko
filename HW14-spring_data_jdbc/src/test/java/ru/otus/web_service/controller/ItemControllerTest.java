package ru.otus.web_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.web_service.controler.ItemController;
import ru.otus.web_service.dto.ErrorDto;
import ru.otus.web_service.dto.ItemDto;
import ru.otus.web_service.model.Item;
import ru.otus.web_service.service.ItemService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.web_service.utils.ItemMappingUtils.itemTo;
import static ru.otus.web_service.utils.ItemMappingUtils.listItemsTo;

@WebMvcTest(ItemController.class)
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ItemControllerTest {
    private static final  Item ITEM_1 = new Item(1L, "first", "desc", 10.0);

    private static final  Item ITEM_2 = new Item(2L, "second", "desc", 20.0);

    private static final  ItemDto ITEM_DTO = itemTo(ITEM_1);

    private static final  ErrorDto EXPECTED_NOT_FOUND_RESPONSE = new ErrorDto(
            "Item with id=1 not found", "uri=/api/v1/items/1"
    );

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create an item and return 201 CREATED")
    public void checkCreateItem() throws Exception {
        when(itemService.create(any(Item.class))).thenReturn(ITEM_1);

        mockMvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ITEM_DTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(ITEM_1)));
    }

    @Test
    @DisplayName("Should return 200 OK with ItemDtos")
    public void checkGetAllItems() throws Exception {
        var items = List.of(ITEM_1, ITEM_2);

        when(itemService.getAll()).thenReturn(items);

        var expectedDtos = listItemsTo(items);

        mockMvc.perform(get("/api/v1/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedDtos)));

    }

    @Test
    @DisplayName("Should return 200 OK for existing item by ID")
    public void checkGetByIdExistingItem() throws Exception {
        when(itemService.getById(anyLong())).thenReturn(Optional.of(ITEM_1));

        mockMvc.perform(get("/api/v1/items/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(ITEM_DTO)));
    }

    @Test
    @DisplayName("Should return 404 Not Found for non-existing item by ID")
    public void checkGetByIdNonExistingItem() throws Exception {
        when(itemService.getById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/items/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(EXPECTED_NOT_FOUND_RESPONSE)));
    }

    @Test
    @DisplayName("Should update an item and return 200 OK for existing item")
    public void checkUpdateExistingItem() throws Exception {
        when(itemService.update(anyLong(), any(Item.class))).thenReturn(Optional.of(ITEM_1));

        mockMvc.perform(put("/api/v1/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ITEM_DTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(ITEM_DTO)));
    }

    @Test
    @DisplayName("Should return 404 Not Found for non-existing item during update")
    public void checkUpdateNonExistingItem() throws Exception {
        when(itemService.update(anyLong(), any(Item.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ITEM_DTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(EXPECTED_NOT_FOUND_RESPONSE)));
    }

    @Test
    @DisplayName("Should delete an item and return 204 No Content for successful deletion")
    public void checkDeleteExistingItem() throws Exception {
        when(itemService.deleteById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/items/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 Not Found for non-existing item during deletion")
    public void checkDeleteNonExistingItem() throws Exception {
        when(itemService.deleteById(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/api/v1/items/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(EXPECTED_NOT_FOUND_RESPONSE)));
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error for unexpected errors")
    public void checkHandleUnexpectedError() throws Exception {
        when(itemService.getById(anyLong())).thenThrow(new RuntimeException("Unexpected error"));
        mockMvc.perform(get("/api/v1/items/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(EXPECTED_NOT_FOUND_RESPONSE)));
    }
}
