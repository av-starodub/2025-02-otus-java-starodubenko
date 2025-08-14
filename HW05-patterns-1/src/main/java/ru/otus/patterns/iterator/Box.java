package ru.otus.patterns.iterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Box implements Iterable<String> {
    private final Collection<List<String>> storage;

    public Box(List<String> first, List<String> second, List<String> third, List<String> fourth) {
        storage = new ArrayList<>();
        storage.add(doCopy(first));
        storage.add(doCopy(second));
        storage.add(doCopy(third));
        storage.add(doCopy(fourth));
    }

    @Override
    public Iterator<String> iterator() {
        return new BoxIterator(new ArrayList<>(storage));
    }

    private List<String> doCopy(List<String> source) {
        Objects.requireNonNull(source, "list must not be null");
        return new ArrayList<>(source);
    }
}
