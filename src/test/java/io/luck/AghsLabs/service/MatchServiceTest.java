package io.luck.AghsLabs.service;

import io.luck.AghsLabs.mapper.MatchDetailsMapper;
import io.luck.AghsLabs.mapper.MatchPlayerMapper;
import io.luck.AghsLabs.model.MatchChat;
import io.luck.AghsLabs.model.MatchWards;
import io.luck.AghsLabs.model.entity.Hero;
import io.luck.AghsLabs.model.entity.Item;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaMatchPlayer;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaMatchDetailsData;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayerMatchDetails;
import io.luck.AghsLabs.providers.opendota.service.OpenDotaPlayerMatchDataService;
import io.luck.AghsLabs.providers.opendota.service.OpenDotaLiveDataService;
import io.luck.AghsLabs.repository.HeroRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class MatchServiceTest {

    @Test
    void wardsKeepPlayerIdentityAndEmptyLogsWithoutHeroOrItemLookups() {
        var provider = mock(OpenDotaPlayerMatchDataService.class);
        var heroes = mock(HeroService.class);
        var items = mock(ItemService.class);
        var service = new MatchService(heroes, mock(HeroRepository.class), items,
                new MatchDetailsMapper(new MatchPlayerMapper()), provider, mock(OpenDotaLiveDataService.class));
        var player = OpenDotaMatchPlayer.builder()
                .playerSlot(128).heroId(7L)
                .obsLog(List.of()).senLog(List.of()).obsLeftLog(List.of()).senLeftLog(List.of())
                .build();
        when(provider.getMatchDetails(123L)).thenReturn(OpenDotaPlayerMatchDetails.builder()
                .matchId(123L).players(List.of(player)).build());

        assertEquals(new MatchWards(List.of(new MatchWards.PlayerWards(
                128, null, 7L, List.of(), List.of(), List.of(), List.of()))), service.getMatchWards(123L));
        verify(provider).getMatchDetails(123L);
        verifyNoInteractions(heroes, items);
    }

    @Test
    void matchWithoutPlayerDataReturnsEmptyWards() {
        var provider = mock(OpenDotaPlayerMatchDataService.class);
        var service = new MatchService(mock(HeroService.class), mock(HeroRepository.class), mock(ItemService.class),
                new MatchDetailsMapper(new MatchPlayerMapper()), provider, mock(OpenDotaLiveDataService.class));
        when(provider.getMatchDetails(123L)).thenReturn(OpenDotaPlayerMatchDetails.builder().matchId(123L).build());

        assertEquals(new MatchWards(List.of()), service.getMatchWards(123L));
    }

    @Test
    void missingMatchHasNoWards() {
        var provider = mock(OpenDotaPlayerMatchDataService.class);
        var service = new MatchService(mock(HeroService.class), mock(HeroRepository.class), mock(ItemService.class),
                new MatchDetailsMapper(new MatchPlayerMapper()), provider, mock(OpenDotaLiveDataService.class));

        assertNull(service.getMatchWards(123L));
        verify(provider).getMatchDetails(123L);
    }

    @Test
    void chatUsesMatchDetailsWithoutHeroOrItemLookups() {
        var provider = mock(OpenDotaPlayerMatchDataService.class);
        var heroes = mock(HeroService.class);
        var items = mock(ItemService.class);
        var service = new MatchService(heroes, mock(HeroRepository.class), items,
                new MatchDetailsMapper(new MatchPlayerMapper()), provider, mock(OpenDotaLiveDataService.class));
        var details = OpenDotaPlayerMatchDetails.builder()
                .matchId(123L)
                .chat(List.of(new OpenDotaMatchDetailsData.Chat(60, "chat", "gg", 0, 0)))
                .players(List.of(OpenDotaMatchPlayer.builder().playerSlot(0).accountId(42L).build()))
                .allWordCounts(Map.of("gg", 1L))
                .myWordCounts(Map.of())
                .build();
        when(provider.getMatchDetails(123L)).thenReturn(details);

        assertEquals(new MatchChat(List.of(new MatchChat.Chat(60, "chat", "gg", 0, 0, 42L)),
                Map.of("gg", 1L), Map.of()), service.getMatchChat(123L));
        verify(provider).getMatchDetails(123L);
        verifyNoInteractions(heroes, items);
    }

    @Test
    void missingMatchHasNoChat() {
        var provider = mock(OpenDotaPlayerMatchDataService.class);
        var service = new MatchService(mock(HeroService.class), mock(HeroRepository.class), mock(ItemService.class),
                new MatchDetailsMapper(new MatchPlayerMapper()), provider, mock(OpenDotaLiveDataService.class));

        assertNull(service.getMatchChat(123L));
        verify(provider).getMatchDetails(123L);
    }

    @Test
    void matchDetailsKeepSixInventorySlotsWithoutLookingUpEmptyItems() {
        var provider = mock(OpenDotaPlayerMatchDataService.class);
        var heroes = mock(HeroService.class);
        var items = mock(ItemService.class);
        var service = new MatchService(heroes, mock(HeroRepository.class), items,
                new MatchDetailsMapper(new MatchPlayerMapper()), provider, mock(OpenDotaLiveDataService.class));

        var player = OpenDotaMatchPlayer.builder()
                .kills(4).assists(8).deaths(2).denies(0).lastHits(120)
                .gold(1000L).goldSpent(5000L).goldPerMin(400).xpPerMin(500)
                .heroId(7L).name("Player").level(20).personaName("Player")
                .isRadiant(true).radiantWin(true).item0(42L).item2(0L)
                .heroHealing(0L).heroDamage(12000L)
                .build();
        var details = OpenDotaPlayerMatchDetails.builder()
                .matchId(9025128045L).cluster(111).duration(1800L).gameMode(1)
                .direScore(10).radiantScore(20).radiantWin(true)
                .players(List.of(player))
                .build();
        var hero = new Hero();
        hero.setId(7L);
        var item = new Item();
        item.setItemId(42L);

        when(provider.getMatchDetails(9025128045L)).thenReturn(details);
        when(heroes.getAllById(Set.of(7L))).thenReturn(List.of(hero));
        when(items.getAllById(Set.of(42L))).thenReturn(List.of(item));

        var result = service.getMatchDetails(9025128045L);
        var itemSlots = result.getRadiantTeam().iterator().next().items();

        assertEquals(Arrays.asList(42L, null, 0L, null, null, null), player.getItems());
        assertEquals(6, itemSlots.size());
        assertEquals(42L, itemSlots.getFirst().getItemId());
        for (int slot = 1; slot < itemSlots.size(); slot++) assertNull(itemSlots.get(slot));
        verify(items).getAllById(Set.of(42L));
    }
}
