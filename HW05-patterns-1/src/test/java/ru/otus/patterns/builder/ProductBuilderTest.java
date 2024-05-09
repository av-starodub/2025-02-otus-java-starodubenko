package ru.otus.patterns.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductBuilderTest")
class ProductBuilderTest {
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

        assertThat(product)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", ID)
                .hasFieldOrPropertyWithValue("title", TITLE)
                .hasFieldOrPropertyWithValue("description", DESCRIPTION)
                .hasFieldOrPropertyWithValue("cost", COST)
                .hasFieldOrPropertyWithValue("weight", WEIGHT)
                .hasFieldOrPropertyWithValue("width", WIDTH)
                .hasFieldOrPropertyWithValue("length", LENGTH)
                .hasFieldOrPropertyWithValue("height", HEIGHT);
    }
}
