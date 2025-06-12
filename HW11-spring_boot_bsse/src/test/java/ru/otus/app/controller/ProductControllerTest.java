package ru.otus.app.controller;

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
import ru.otus.app.dto.ExceptionDto;
import ru.otus.app.dto.ProductDto;
import ru.otus.app.dto.ProductsDto;
import ru.otus.app.model.Product;
import ru.otus.app.service.ProductService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ProductControllerTest {

    private static final Product PRODUCT_1 = new Product(1L, "Product1", 10.0);

    private static final Product PRODUCT_2 = new Product(2L, "Product2", 20.0);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("Should create a new product and return ProductDto with status 201 Created")
    void checkCreateProduct() throws Exception {
        var createdProduct = PRODUCT_1;
        var expectedProductDto = new ProductDto(createdProduct.getTitle(), createdProduct.getPrice());

        when(productService.create(any(ProductDto.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedProductDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(createdProduct)));
    }

    @Test
    @DisplayName("Should return a ProductDto by ID when product exists with status 200 OK")
    void checkGetProductByIdWhenProductExists() throws Exception {
        var selectedProduct = PRODUCT_1;
        var expectedProductDto = new ProductDto(selectedProduct.getTitle(), selectedProduct.getPrice());

        when(productService.getById(anyLong())).thenReturn(Optional.of(selectedProduct));

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProductDto)));
    }

    @Test
    @DisplayName("Should return 404 Not Found when the product does not exist")
    void checkGetProductByWhenProductDoesNotExist() throws Exception {
        when(productService.getById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return a list of all products with status 200 OK")
    void checkGetAllProducts() throws Exception {
        var products = List.of(PRODUCT_1, PRODUCT_2);
        var productsDto = products.stream()
                .map(product -> new ProductDto(product.getTitle(), product.getPrice()))
                .toList();

        when(productService.getAll()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ProductsDto(productsDto))));
    }

    @Test
    @DisplayName("should return 500 Internal Server Error when an unexpected exception occurs")
    void checkHandleUnexpectedError() throws Exception {
        when(productService.getById(1L)).thenThrow(new RuntimeException("Unexpected error"));

        var expectedResponse = new ExceptionDto("Unexpected error", "uri=/api/v1/products/1");

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }
}
