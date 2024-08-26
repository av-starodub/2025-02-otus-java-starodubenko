package ru.otus.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import ru.otus.app.dto.ExceptionDto;
import ru.otus.app.dto.ProductDto;
import ru.otus.app.dto.ProductsDto;
import ru.otus.app.exception.ProductNotFoundException;
import ru.otus.app.model.Product;
import ru.otus.app.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
        var createdProduct = productService.create(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) {
        var product = productService.getById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID=%d".formatted(id)));
        var productDto = new ProductDto(product.getTitle(), product.getPrice());
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ProductsDto> getAllProducts() {
        var products = productService.getAll();
        var productsDto = products.stream()
                .map(product -> new ProductDto(product.getTitle(), product.getPrice()))
                .toList();
        return new ResponseEntity<>(new ProductsDto(productsDto), HttpStatus.OK);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
        var exceptionDto = new ExceptionDto(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleAllExceptions(Exception ex, WebRequest request) {
        var exceptionDto = new ExceptionDto(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
