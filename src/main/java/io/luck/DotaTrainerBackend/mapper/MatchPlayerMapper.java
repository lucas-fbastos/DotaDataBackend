package io.luck.DotaTrainerBackend.mapper;

import io.luck.DotaTrainerBackend.model.entity.Hero;
import io.luck.DotaTrainerBackend.model.entity.Item;
import io.luck.DotaTrainerBackend.model.MatchPlayer;
import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaMatchPlayer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MatchPlayerMapper {

    public Set<MatchPlayer> convert(Collection<OpenDotaMatchPlayer> openDotaMatchPlayers,
                                    Map<Long, Hero> heroesById,
                                    Map<Long, Item> itemsById) {
        Set<MatchPlayer> players = new HashSet<>();
        openDotaMatchPlayers.forEach(openDotaMatchPlayer -> {
            Hero hero = heroesById.get(openDotaMatchPlayer.heroId());
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
                    .items(processItems(openDotaMatchPlayer, itemsById))
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

    private List<Item> processItems(OpenDotaMatchPlayer openDotaMatchPlayer, Map<Long, Item> itemsById) {
        return openDotaMatchPlayer.getItems()
                .stream()
                .map(itemsById::get)
                .collect(Collectors.toList());
    }
}
