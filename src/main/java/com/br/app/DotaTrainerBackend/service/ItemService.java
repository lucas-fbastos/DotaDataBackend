package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.domain.Item;
import com.br.app.DotaTrainerBackend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ItemService extends BaseService {

    @Autowired
    private ItemRepository itemRepository;

    public Item getById(Long itemId){
         return this.itemRepository.findById(itemId).orElseThrow();
    }

    public List<Item> getAllById(Set<Long> ids){
        return itemRepository.findAllById(ids);
    }
}
