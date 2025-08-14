package ru.otus.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectFactory;
import ru.otus.app.model.Product;
import ru.otus.app.repository.Dao;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    private static final List<Product> PRODUCTS = List.of(
            new Product(1L, "Product 1", 50.0),
            new Product(2L, "Product 2", 150.0)
    );

    private static final Product PRODUCT = PRODUCTS.get(0);

    private Cart cart;

    @Mock
    private Dao productRepository;


    @Mock
    private ObjectFactory<Cart> cartFactory;

    @BeforeEach
    void setCart() {
        cart = new Cart();
    }

    @InjectMocks
    private CartService cartService;

    @Test
    @DisplayName("Should add product to cart successfully")
    void testAddProductToCartSuccess() {
        var productId = PRODUCT.getId();

        when(productRepository.findById(productId)).thenReturn(Optional.of(PRODUCT));

        var result = cartService.add(productId, cart);

        assertThat(result)
                .isNotNull()
                .isEqualTo(PRODUCT);
    }

    @Test
    @DisplayName("Should return null when product not found while adding to cart")
    void checkProductNotFound() {
        var productId = PRODUCT.getId();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        var result = cartService.add(productId, cart);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should return null when adding product to cart fails")
    void testAddProductToCart_AddProductFails() {
        var cartMock = Mockito.mock(Cart.class);

        var productId = PRODUCT.getId();

        when(productRepository.findById(productId)).thenReturn(Optional.of(PRODUCT));
        when(cartMock.addProduct(PRODUCT)).thenReturn(false);

        var result = cartService.add(productId, cartMock);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should remove product from cart successfully")
    void testRemoveProductFromCart_Success() {
        cart.addProduct(PRODUCT);
        var productId = PRODUCT.getId();
        var result = cartService.remove(productId, cart);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when removing product from cart fails")
    void testRemoveProductFromCart_Fails() {
        var productId = PRODUCT.getId();
        var result = cartService.remove(productId, cart);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should show cart products successfully")
    void testShowCart() {
        PRODUCTS.forEach(product -> cart.addProduct(product));
        var productsFromCart = cartService.showCart(cart);
        assertThat(productsFromCart).containsExactlyInAnyOrderElementsOf(PRODUCTS);
    }

    @Test
    @DisplayName("Should get all products from repository")
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(PRODUCTS);

        var productList = cartService.getAllProducts();

        verify(productRepository).findAll();

        assertThat(productList).containsExactlyInAnyOrderElementsOf(PRODUCTS);
    }

    @Test
    @DisplayName("Should create a new cart")
    void testCreateNewCart() {
        var newCart = new Cart();

        when(cartFactory.getObject()).thenReturn(newCart);

        var result = cartService.createNewCart();

        verify(cartFactory).getObject();

        assertThat(result).isSameAs(newCart);
    }
}
