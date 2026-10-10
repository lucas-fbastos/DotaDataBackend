package io.luck.AghsLabs.service;


import io.luck.AghsLabs.model.entity.Hero;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaHeroStats;
import io.luck.AghsLabs.mapper.PlayerMapper;
import io.luck.AghsLabs.model.HeroStats;
import io.luck.AghsLabs.model.entity.Patch;
import io.luck.AghsLabs.model.entity.Player;
import io.luck.AghsLabs.providers.opendota.service.OpenDotaPlayerDataService;
import io.luck.AghsLabs.repository.PatchRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private static final Logger LOGGER = Logger.getLogger(PlayerService.class.getName());

    private final HeroService heroService;

    private final PatchRepository patchRepository;

    private final OpenDotaPlayerDataService openDotaPlayerDataService;

    public PlayerService(HeroService heroService, PatchRepository patchRepository, OpenDotaPlayerDataService openDotaPlayerDataService){
        this.heroService = heroService;
        this.patchRepository = patchRepository;
        this.openDotaPlayerDataService = openDotaPlayerDataService;

    }

    public Player getPlayerById(@NonNull Long playerId){
        var openDotaPlayer = openDotaPlayerDataService.getById(playerId);
        return PlayerMapper.toPlayer(openDotaPlayer);
    }

    public List<HeroStats> getPlayerHeroes(long playerId, Integer pageSize, Integer page) {

        Patch lastPatch = patchRepository.getLatestPatch();

        assert lastPatch !=null;

        LOGGER.info("Getting player heroes for player "+playerId);

        var apiHeroStats = openDotaPlayerDataService.getPlayerHeroes(
                playerId,
                lastPatch.getId(),
                pageSize,
                page
        );

        assert apiHeroStats != null;

        var heroIds = apiHeroStats.stream()
                .skip(page)
                .limit(pageSize)
                .map(OpenDotaHeroStats::heroId)
                .collect(Collectors.toSet());

        Map<Long, Hero> heroMap = heroService.getAllById(heroIds)
                .stream()
                .collect(Collectors.toMap(Hero::getId, Function.identity()));

        return apiHeroStats.stream().map(apiHero ->
                new HeroStats(
                    heroMap.get(apiHero.heroId()),
                    apiHero.games(),
                        apiHero.win(),
                        apiHero.withGames(),
                        apiHero.winWith(),
                        apiHero.againstGames(),
                        apiHero.againstWin()
                )
        ).toList();
    }
}
