package ru.otus.patterns.iterator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("BoxIteratorTest")
class BoxIteratorTest {

    @Test
    @DisplayName("checkIterator - should return all strings from the lists of the box sequentially")
    void checkIterator() {
        var box = new Box(List.of("1", "2"), List.of("3"), List.of(), List.of("4", "5"));
        var result = new ArrayList<String>();
        for (var element : box) {
            result.add(element);
        }
        assertThat(result)
                .hasSize(5)
                .containsSequence("1", "2", "3", "4", "5");
    }

    @Test
    @DisplayName("checkNextWithoutHasNext - should throw NoSuchElementException when the index is out of bounds and hasNext() is not called")
    void checkNextWithoutHasNext() {
        var box = new Box(List.of("1"), List.of(), List.of(), List.of());
        var boxIterator = box.iterator();
        var result = new ArrayList<String>();

        if (boxIterator.hasNext()) {
            result.add(boxIterator.next());
        }

        var thrown = catchThrowable(() -> result.add(boxIterator.next()));

        assertThat(thrown)
                .isInstanceOf(NoSuchElementException.class)
                .hasRootCauseInstanceOf(IndexOutOfBoundsException.class);

        if (boxIterator.hasNext()) {
            result.add(boxIterator.next());
        }

        assertThat(result)
                .hasSize(1)
                .containsOnly("1");
    }
}
