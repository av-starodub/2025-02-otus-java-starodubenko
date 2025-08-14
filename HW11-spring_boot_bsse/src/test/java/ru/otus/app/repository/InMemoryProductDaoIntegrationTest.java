package ru.otus.app.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.app.config.ProductProperties;
import ru.otus.app.exception.DataLoadException;
import ru.otus.app.loader.ProductLoader;
import ru.otus.app.model.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@ActiveProfiles("test")
public class InMemoryProductDaoIntegrationTest {
    @Autowired
    private ProductProperties productProperties;

    @Autowired
    private ProductLoader productLoader;

    @Autowired
    private InMemoryProductDao inMemoryProductDao;


    private static final String VALID_PRODUCTS_FILE = "valid-products-test.json";
    private static final String INVALID_PRODUCTS_FILE = "invalid-products-test.json";
    private static final String NON_EXISTENT_FILE = "non-existent-file.json";

    private static final double TEST_PRODUCT_PRICE = 10.45;
    private static final String TEST_PRODUCT_TITLE = "New Product";
    private static final Product TEST_PRODUCT = new Product(null, TEST_PRODUCT_TITLE, TEST_PRODUCT_PRICE);

    @BeforeEach
    public void setUp() {
        productProperties.setFileName(VALID_PRODUCTS_FILE);
        inMemoryProductDao.init();
    }

    @Test
    @DisplayName("Should load valid products")
    public void checkLoadValidProducts() {
        var products = inMemoryProductDao.findAll();
        assertThat(products)
                .isNotEmpty()
                .hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Should throw DataLoadException for non-existent file")
    public void checkLoadProductsWithNonExistentFile() {
        productProperties.setFileName(NON_EXISTENT_FILE);
        assertThatExceptionOfType(DataLoadException.class)
                .isThrownBy(() -> productLoader.loadProducts())
                .withMessageContaining("File not found: " + NON_EXISTENT_FILE);
    }

    @Test
    @DisplayName("Should throw DataLoadException for invalid JSON format")
    public void checkLoadInvalidJsonFormat() {
        productProperties.setFileName(INVALID_PRODUCTS_FILE);
        assertThatExceptionOfType(DataLoadException.class)
                .isThrownBy(() -> productLoader.loadProducts())
                .withMessageContaining("JSON mapping error in file: " + INVALID_PRODUCTS_FILE);
    }

    @Test
    @DisplayName("Should insert a product")
    public void checkInsertProduct() {
        var savedProduct = inMemoryProductDao.insert(TEST_PRODUCT);
        assertThat(savedProduct)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", TEST_PRODUCT_TITLE)
                .hasFieldOrPropertyWithValue("price", TEST_PRODUCT_PRICE);
        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    @DisplayName("Should find product by ID")
    public void checkFindById() {
        var products = inMemoryProductDao.findAll();

        assertThat(products).isNotEmpty();

        var existingProduct = products.get(0);
        var selectedProduct = inMemoryProductDao.findById(existingProduct.getId());

        assertThat(selectedProduct).isPresent();
        assertThat(selectedProduct.get())
                .hasFieldOrPropertyWithValue("title", existingProduct.getTitle())
                .hasFieldOrPropertyWithValue("price", existingProduct.getPrice());
    }

    @Test
    @DisplayName("Should find all products")
    public void checkFindAll() {
        var products = inMemoryProductDao.findAll();
        assertThat(products).isNotEmpty();
        assertThat(products).hasSizeGreaterThan(0);
    }
}
