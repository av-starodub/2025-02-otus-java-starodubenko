package ru.otus.app.util;

import ru.otus.app.dto.ProductDto;
import ru.otus.app.dto.ProductsDto;
import ru.otus.app.model.Product;

import java.util.List;

public final class ProductMapUtils {

    private ProductMapUtils() {
    }

    public static ProductDto productTo(Product product) {
        return new ProductDto(product.getTitle(), product.getPrice());
    }

    public static ProductsDto productsListTo(List<Product> products) {
        var listDtos = products.stream()
                .map(ProductMapUtils::productTo)
                .toList();
        return new ProductsDto(listDtos);
    }
}
