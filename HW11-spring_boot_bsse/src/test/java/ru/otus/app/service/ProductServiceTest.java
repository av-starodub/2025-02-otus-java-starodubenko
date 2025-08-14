package ru.otus.app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.app.dto.ProductDto;
import ru.otus.app.model.Product;
import ru.otus.app.repository.ProductDao;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    // Test data
    private static final Long PRODUCT_ID = 1L;
    private static final String PRODUCT_TITLE = "Test Product";
    private static final double PRODUCT_PRICE = 100.0;
    private static final ProductDto PRODUCT_DTO = new ProductDto(PRODUCT_TITLE, PRODUCT_PRICE);
    private static final Product PRODUCT = new Product(PRODUCT_ID, PRODUCT_TITLE, PRODUCT_PRICE);
    private static final List<Product> PRODUCTS = List.of(
            new Product(1L, "Product 1", 50.0),
            new Product(2L, "Product 2", 150.0)
    );
    private static final List<ProductDto> PRODUCT_DTOS = List.of(
            new ProductDto("Product 1", 50.0),
            new ProductDto("Product 2", 150.0)
    );

    @Test
    @DisplayName("Should create a new product from ProductDto")
    void checkCreateProduct() {
        when(productDao.insert(any(Product.class))).thenReturn(PRODUCT);

        var createdProduct = productService.create(PRODUCT_DTO);

        assertThat(createdProduct)
                .isNotNull()
                .isEqualTo(PRODUCT);

        verify(productDao).insert(any(Product.class));
    }

    @Test
    @DisplayName("Should return Product when product is found by ID")
    void checkGetByIdSuccess() {
        when(productDao.findById(PRODUCT_ID)).thenReturn(Optional.of(PRODUCT));

        var selectedProduct = productService.getById(PRODUCT_ID);

        assertThat(selectedProduct)
                .isPresent()
                .hasValue(PRODUCT);

        verify(productDao).findById(PRODUCT_ID);
    }

    @Test
    @DisplayName("Should return List with all products")
    void checkGetAllProducts() {
        when(productDao.findAll()).thenReturn(PRODUCTS);

        var selectedProducts = productService.getAll();

        assertThat(selectedProducts)
                .isNotNull()
                .isEqualTo(PRODUCTS);

        verify(productDao).findAll();
    }
}
