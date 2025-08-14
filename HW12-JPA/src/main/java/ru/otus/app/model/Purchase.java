package ru.otus.app.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "purchases")
@NamedEntityGraph(
        name = "Purchase.withClientAndProduct",
        attributeNodes = {
                @NamedAttributeNode("client"),
                @NamedAttributeNode("product")
        }
)
public class Purchase extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Purchase(Long id, Client client, Product product, BigDecimal price, LocalDateTime createdAt) {
        super(id);
        this.client = client;
        this.product = product;
        this.price = price;
        this.createdAt = createdAt;
    }

    public void setPriceAtTheTimeOfPurchase() {
        product.setPrice(price);
    }

    @Override
    public String toString() {
        return "Purchase{id=%s, %s, %s}".formatted(super.getId(), client, product);
    }
}
