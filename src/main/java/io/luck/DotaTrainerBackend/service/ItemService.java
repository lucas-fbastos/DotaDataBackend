package io.luck.DotaTrainerBackend.service;

import io.luck.DotaTrainerBackend.model.entity.Item;
import io.luck.DotaTrainerBackend.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item getById(Long itemId){
         return this.itemRepository.findById(itemId).orElseThrow();
    }

    public List<Item> getAllById(Set<Long> ids){
        return itemRepository.findAllById(ids);
    }
}
