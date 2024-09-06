package ru.otus.app.dto;

import lombok.Data;
import ru.otus.app.model.Client;
import ru.otus.app.model.Product;
import ru.otus.app.model.Purchase;

import java.time.LocalDateTime;

@Data
public class PurchaseDto implements Dto<Purchase> {

    private Long clientId;

    private Long productId;

    private LocalDateTime createdAt;

    @Override
    public Purchase toDomainObject() {
        var client = new Client();
        client.setId(clientId);
        var product = new Product();
        product.setId(productId);
        return new Purchase(null, client, product, createdAt);
    }
}
