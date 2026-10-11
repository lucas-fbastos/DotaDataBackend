package io.luck.AghsLabs.mapper;

import io.luck.AghsLabs.model.Enums.Faction;
import io.luck.AghsLabs.model.LiveMatch;
import io.luck.AghsLabs.model.entity.Hero;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaLiveMatch;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class LiveMatchMapper {

    private LiveMatchMapper() {}

    public static Set<LiveMatch> convert(List<OpenDotaLiveMatch> matches, Set<Hero> heroes) {
        if (matches == null || matches.isEmpty()) return Collections.emptySet();

        Map<Long, Hero> heroesById = heroes == null ? Collections.emptyMap()
                : heroes.stream().collect(Collectors.toMap(Hero::getId, Function.identity()));

        return matches.stream()
                .map(match -> LiveMatch.builder()
                        .matchId(match.matchId())
                        .duration(match.gameTime() == null ? null : Duration.ofSeconds(match.gameTime()))
                        .radiantScore(match.radiantScore())
                        .direScore(match.direScore())
                        .averageMmr(match.averageMmr())
                        .radiantHeroImages(mapHeroImages(match.players(), Faction.RADIANT.getId(), heroesById))
                        .direHeroImages(mapHeroImages(match.players(), Faction.DIRE.getId(), heroesById))
                        .build())
                .collect(Collectors.toSet());
    }

    private static List<String> mapHeroImages(
            List<OpenDotaLiveMatch.OpenDotaLivePlayer> players,
            int team,
            Map<Long, Hero> heroesById) {
        if (players == null) return Collections.emptyList();

        return players.stream()
                .filter(player -> player.team() != null && player.team() == team)
                .map(player -> heroesById.get(player.heroId()))
                .filter(hero -> hero != null && hero.getImage() != null)
                .map(Hero::getImage)
                .toList();
    }
}
