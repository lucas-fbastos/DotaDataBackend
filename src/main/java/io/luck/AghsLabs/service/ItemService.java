package io.luck.AghsLabs.service;

import io.luck.AghsLabs.model.entity.Item;
import io.luck.AghsLabs.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllById(Set<Long> ids){
        return itemRepository.findAllById(ids);
    }

    public Map<Long, Item> getByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return Map.of();

        return itemRepository.findAllById(ids).stream()
                .collect(Collectors.toUnmodifiableMap(Item::getItemId, Function.identity()));
    }
}
