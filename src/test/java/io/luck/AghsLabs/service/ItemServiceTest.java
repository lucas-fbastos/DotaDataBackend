package io.luck.AghsLabs.service;

import io.luck.AghsLabs.model.entity.Item;
import io.luck.AghsLabs.repository.ItemRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ItemServiceTest {

    @Test
    void getByIdsFetchesOnlyRequestedItemsAndIndexesThemById() {
        var repository = mock(ItemRepository.class);
        var service = new ItemService(repository);
        var requestedIds = Set.of(42L, 84L);
        var first = item(42L, "Blink Dagger");
        var second = item(84L, "Black King Bar");
        when(repository.findAllById(requestedIds)).thenReturn(List.of(first, second));

        var result = service.getByIds(requestedIds);

        assertEquals(Map.of(42L, first, 84L, second), result);
        verify(repository).findAllById(requestedIds);
    }

    @Test
    void getByIdsSkipsTheDatabaseForAnEmptyIdSet() {
        var repository = mock(ItemRepository.class);
        var service = new ItemService(repository);

        assertEquals(Map.of(), service.getByIds(Set.of()));
        verifyNoInteractions(repository);
    }

    private Item item(Long id, String name) {
        var item = new Item();
        item.setItemId(id);
        item.setName(name);
        return item;
    }
}
