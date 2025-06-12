package ru.otus.web_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.web_service.model.Item;
import ru.otus.web_service.repository.ItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item create(@NonNull Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Optional<Item> getById(@NonNull Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> getAll() {
        return StreamSupport.stream(itemRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> update(@NonNull Long id, @NonNull Item item) {
        var optionalItem = itemRepository.findById(id);
        return optionalItem.map(existingItem -> {
            existingItem.setName(item.getName());
            existingItem.setDescription(item.getDescription());
            existingItem.setPrice(item.getPrice());
            return itemRepository.save(existingItem);
        });
    }

    @Override
    public boolean deleteById(@NonNull Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
