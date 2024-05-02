package ru.otus.patterns.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductBuilderTest")
public class ProductBuilderTest {
    private static final long ID = 1;
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "desc";
    private static final double COST = 100;
    private static final double WEIGHT = 0.5;
    private static final double WIDTH = 1.5;
    private static final double LENGTH = 2.5;
    private static final double HEIGHT = 1.3;

    @Test
    @DisplayName("checkBuilder - should build Product correctly")
    void checkBuilder() {
        var product = Product.builder()
                .id(ID)
                .title(TITLE)
                .desc(DESCRIPTION)
                .cost(COST)
                .weight(WEIGHT)
                .width(WIDTH)
                .length(LENGTH)
                .height(HEIGHT)
                .build();

        assertThat(product.getId()).isEqualTo(ID);
        assertThat(product.getTitle()).isEqualTo(TITLE);
        assertThat(product.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(product.getCost()).isEqualTo(COST);
        assertThat(product.getWeight()).isEqualTo(WEIGHT);
        assertThat(product.getWidth()).isEqualTo(WIDTH);
        assertThat(product.getLength()).isEqualTo(LENGTH);
        assertThat(product.getHeight()).isEqualTo(HEIGHT);
    }
}
