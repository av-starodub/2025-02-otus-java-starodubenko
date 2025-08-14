package ru.otus.patterns.iterator;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class BoxIterator implements Iterator<String> {
    private final String[] elements;
    private int cursor;

    public BoxIterator(Collection<List<String>> box) {
        elements = box.stream()
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .toArray(String[]::new);
    }

    @Override
    public boolean hasNext() {
        return cursor != elements.length;
    }

    @Override
    public String next() {
        try {
            var element = elements[cursor];
            cursor++;
            return element;
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException(e);
        }
    }
}
