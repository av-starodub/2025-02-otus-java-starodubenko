package ru.otus.patterns.builder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private long id;
    private String title;
    private String description;
    private double cost;
    private double weight;
    private double width;
    private double length;
    private double height;

    private Product() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Product product;

        private Builder() {
            product = new Product();
        }

        public Builder id(long iD) {
            product.id = iD;
            return this;
        }

        public Builder title(String ttl) {
            product.title = ttl;
            return this;
        }

        public Builder desc(String desc) {
            product.description = desc;
            return this;
        }

        public Builder cost(double cst) {
            product.cost = cst;
            return this;
        }

        public Builder weight(double wgh) {
            product.weight = wgh;
            return this;
        }

        public Builder width(double wdt) {
            product.width = wdt;
            return this;
        }

        public Builder length(double ln) {
            product.length = ln;
            return this;
        }

        public Builder height(double hgh) {
            product.height = hgh;
            return this;
        }

        public Product build() {
            return product;
        }
    }
}
