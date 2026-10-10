package io.luck.DotaTrainerBackend.mapper;

import io.luck.DotaTrainerBackend.model.entity.Hero;
import io.luck.DotaTrainerBackend.model.entity.Item;
import io.luck.DotaTrainerBackend.model.MatchPlayer;
import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaMatchPlayer;
import io.luck.DotaTrainerBackend.service.HeroService;
import io.luck.DotaTrainerBackend.service.ItemService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MatchPlayerMapper{

    @Autowired
    private HeroService heroService;
    @Autowired
    private ItemService itemService;

    public Set<MatchPlayer> convert(Collection<OpenDotaMatchPlayer> openDotaMatchPlayers) {
        Set<MatchPlayer> players = new HashSet<>();

        Map<Long, Hero> heroesMap = getHeroesMap(openDotaMatchPlayers);
        Map<Long, Item> itemsMap = getItemsMap(openDotaMatchPlayers);
        openDotaMatchPlayers.forEach(openDotaMatchPlayer ->{
            Hero hero = heroesMap.get(openDotaMatchPlayer.heroId());
            MatchPlayer matchPlayer = MatchPlayer.builder()
                    .kills(openDotaMatchPlayer.kills())
                    .assists(openDotaMatchPlayer.assists())
                    .deaths(openDotaMatchPlayer.deaths())
                    .denies(openDotaMatchPlayer.denies())
                    .lastHits(openDotaMatchPlayer.lastHits())
                    .gold(openDotaMatchPlayer.gold())
                    .goldSpent(openDotaMatchPlayer.goldSpent())
                    .goldPerMin(openDotaMatchPlayer.goldPerMin())
                    .xpPerMin(openDotaMatchPlayer.xpPerMin())
                    .hero(hero)
                    .items(processItems(openDotaMatchPlayer,itemsMap))
                    .name(openDotaMatchPlayer.name())
                    .level(openDotaMatchPlayer.level())
                    .personaName(openDotaMatchPlayer.personaName())
                    .team(openDotaMatchPlayer.getTeam().name())
                    .won(openDotaMatchPlayer.playerWon())
                    .heroHeal(openDotaMatchPlayer.heroHeal())
                    .heroDamage(openDotaMatchPlayer.heroDamage())
            .build();
            players.add(matchPlayer);
        });
        return players;
    }

    private Map<Long, Hero> getHeroesMap(Collection<OpenDotaMatchPlayer> openDotaMatchPlayers){
        val heroesIds = openDotaMatchPlayers.stream()
                .map(OpenDotaMatchPlayer::heroId)
                .collect(Collectors.toSet());

        List<Hero> heroes = heroService.getAllById(heroesIds);
        Map<Long,Hero> heroesMap = new HashMap<>();
        heroes.forEach(hero -> heroesMap.put(hero.getId(),hero));
        return heroesMap;
    }

    private Map<Long, Item> getItemsMap(Collection<OpenDotaMatchPlayer> openDotaMatchPlayers){
        Set<Long> itemIds = openDotaMatchPlayers.stream()
                .flatMap(player -> player.getItems().stream())
                        .collect(Collectors.toSet());

        List<Item> items = itemService.getAllById(itemIds);
        Map<Long,Item> itemsMap = new HashMap<>();
        items.forEach(item -> itemsMap.put(item.getItemId(),item));
        return itemsMap;
    }

    private List<Item> processItems(OpenDotaMatchPlayer openDotaMatchPlayer ,Map<Long,Item> itemsMap) {
        return openDotaMatchPlayer.getItems()
                .stream()
                .map(itemId -> itemsMap.getOrDefault(itemId,null))
                .collect(Collectors.toList());
    }
}
