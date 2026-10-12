package io.luck.AghsLabs.service;

import io.luck.AghsLabs.config.CacheConfig;
import io.luck.AghsLabs.exception.ResourceNotFoundException;
import io.luck.AghsLabs.model.dtos.ComparisonDtos.*;
import io.luck.AghsLabs.model.entity.Hero;
import io.luck.AghsLabs.model.entity.Item;
import io.luck.AghsLabs.model.entity.Player;
import io.luck.AghsLabs.model.entity.ProPlayer;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayerMatchCandidate;
import io.luck.AghsLabs.providers.opendota.service.OpenDotaPlayerMatchDataService;
import io.luck.AghsLabs.repository.ProPlayerRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ComparisonService {


    private final PlayerService playerService;
    private final HeroService heroService;
    private final ItemService itemService;
    private final ProPlayerRepository proPlayerRepository;
    private final OpenDotaPlayerMatchDataService matchDataService;
    private final MatchPairingService pairingService;

    public ComparisonService(PlayerService playerService,
                             HeroService heroService,
                             ItemService itemService,
                             ProPlayerRepository proPlayerRepository,
                             OpenDotaPlayerMatchDataService matchDataService,
                             MatchPairingService pairingService) {
        this.playerService = playerService;
        this.heroService = heroService;
        this.itemService = itemService;
        this.proPlayerRepository = proPlayerRepository;
        this.matchDataService = matchDataService;
        this.pairingService = pairingService;
    }

    @Cacheable(cacheNames = CacheConfig.COMPARISON_OVERVIEWS,
            key = "#leftId + ':' + #rightId + ':' + #candidateLimit + ':' + #pairLimit",
            unless = "#result == null || !#result.cacheable()")
    public ComparisonOverview getOverview(Long leftId, Long rightId, int candidateLimit, int pairLimit) {


        Player leftPlayer = requirePlayer(leftId);
        Player rightPlayer = requirePlayer(rightId);
        Map<Long, ProPlayer> prosById = proPlayerRepository.findAllById(Set.of(leftId, rightId)).stream()
                .collect(Collectors.toMap(ProPlayer::getAccountId, Function.identity()));

        List<OpenDotaPlayerMatchCandidate> rawLeft = safeList(matchDataService.getPlayerMatches(leftId, candidateLimit));
        List<OpenDotaPlayerMatchCandidate> rawRight = safeList(matchDataService.getPlayerMatches(rightId, candidateLimit));
        List<OpenDotaPlayerMatchCandidate> leftCandidates = eligibleUnique(rawLeft, candidateLimit);
        List<OpenDotaPlayerMatchCandidate> rightCandidates = eligibleUnique(rawRight, candidateLimit);
        var pairedCandidates = pairingService.pair(leftCandidates, rightCandidates, pairLimit);

        Set<Long> heroIds = java.util.stream.Stream.concat(leftCandidates.stream(), rightCandidates.stream())
                .map(OpenDotaPlayerMatchCandidate::heroId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, Hero> heroesById = heroService.getAllById(heroIds).stream()
                .collect(Collectors.toMap(Hero::getId, Function.identity()));

        Set<Long> itemIds = pairedCandidates.stream()
                .flatMap(pair -> Stream.concat(pair.left().items().stream(), pair.right().items().stream()))
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());
        Map<Long, Item> itemsById = itemService.getByIds(itemIds);

        List<MatchPair> pairs = pairedCandidates.stream()
                .map(pair -> MatchPair.builder()
                        .left(toOverviewMatch(pair.left(), heroesById, itemsById))
                        .right(toOverviewMatch(pair.right(), heroesById, itemsById))
                        .qualityScore(pair.qualityScore())
                        .compatibilityTier(pair.compatibilityTier())
                        .compatibilityReasons(pair.compatibilityReasons())
                        .deepComparisonAvailable(pair.deepComparisonAvailable())
                        .build())
                .toList();

        List<String> warnings = overviewWarnings(rawLeft.size(), leftCandidates.size(),
                rawRight.size(), rightCandidates.size(), pairs);
        return ComparisonOverview.builder()
                .leftPlayer(toComparisonPlayer(leftPlayer, prosById.get(leftId)))
                .rightPlayer(toComparisonPlayer(rightPlayer, prosById.get(rightId)))
                .leftCandidateCount(leftCandidates.size())
                .rightCandidateCount(rightCandidates.size())
                .leftHeroAggregates(heroAggregates(leftCandidates, heroesById))
                .rightHeroAggregates(heroAggregates(rightCandidates, heroesById))
                .pairs(pairs)
                .warnings(warnings)
                .build();
    }



    private Player requirePlayer(Long playerId) {
        Player player = playerService.getPlayerById(playerId);
        if (player == null) throw new ResourceNotFoundException("Player " + playerId + " was not found");
        return player;
    }

    private List<OpenDotaPlayerMatchCandidate> eligibleUnique(
            List<OpenDotaPlayerMatchCandidate> candidates, int limit) {
        Map<Long, OpenDotaPlayerMatchCandidate> unique = new LinkedHashMap<>();
        candidates.stream()
                .limit(limit)
                .filter(pairingService::isEligible)
                .forEach(candidate -> unique.putIfAbsent(candidate.matchId(), candidate));
        return List.copyOf(unique.values());
    }

    private ComparisonPlayer toComparisonPlayer(Player player, ProPlayer pro) {
        return ComparisonPlayer.builder()
                .accountId(player.getAccountId())
                .personaName(player.getPersonaName())
                .avatarFull(player.getAvatarImageFull())
                .rankTier(player.getRankTier())
                .leaderboardRank(player.getLeaderboardRank())
                .pro(pro != null)
                .teamName(pro == null ? null : pro.getTeamName())
                .teamTag(pro == null ? null : pro.getTeamTag())
                .build();
    }

    private List<HeroAggregate> heroAggregates(List<OpenDotaPlayerMatchCandidate> candidates,
                                               Map<Long, Hero> heroesById) {
        return candidates.stream()
                .collect(Collectors.groupingBy(OpenDotaPlayerMatchCandidate::heroId))
                .entrySet().stream()
                .map(entry -> {
                    long games = entry.getValue().size();
                    long wins = entry.getValue().stream()
                            .filter(match -> Boolean.TRUE.equals(match.playerWon()))
                            .count();
                    Hero hero = heroesById.get(entry.getKey());
                    return HeroAggregate.builder()
                            .heroId(entry.getKey())
                            .heroName(hero == null ? null : hero.getLocalizedName())
                            .heroImage(hero == null ? null : hero.getImage())
                            .games(games)
                            .wins(wins)
                            .winRate(games == 0 ? 0 : (double) wins / games)
                            .build();
                })
                .sorted(java.util.Comparator.comparingLong(HeroAggregate::games).reversed()
                        .thenComparing(HeroAggregate::heroId))
                .toList();
    }

    private OverviewMatch toOverviewMatch(OpenDotaPlayerMatchCandidate match,
                                          Map<Long, Hero> heroesById,
                                          Map<Long, Item> itemsById) {
        Hero hero = heroesById.get(match.heroId());
        return OverviewMatch.builder()
                .matchId(match.matchId())
                .playerSlot(match.playerSlot())
                .heroId(match.heroId())
                .heroName(hero == null ? null : hero.getLocalizedName())
                .heroImage(hero == null ? null : hero.getImage())
                .won(match.playerWon())
                .durationSeconds(match.duration())
                .startTime(match.startTime())
                .patch(match.patch())
                .laneRole(match.laneRole())
                .gameMode(match.gameMode())
                .lobbyType(match.lobbyType())
                .kills(match.kills())
                .deaths(match.deaths())
                .assists(match.assists())
                .lastHits(match.lastHits())
                .denies(match.denies())
                .goldPerMinute(match.goldPerMinute())
                .xpPerMinute(match.xpPerMinute())
                .netWorth(match.netWorth())
                .items(itemSummaries(match.items(), itemsById))
                .build();
    }

    static List<ItemSummary> itemSummaries(List<Long> itemIds, Map<Long, Item> itemsById) {
        if (itemIds == null) return List.of();
        List<ItemSummary> items = new ArrayList<>();
        for (int slot = 0; slot < itemIds.size(); slot++) {
            Long itemId = itemIds.get(slot);
            if (itemId == null || itemId <= 0) continue;
            Item item = itemsById.get(itemId);
            items.add(ItemSummary.builder()
                    .slot(slot)
                    .itemId(itemId)
                    .name(item == null ? null : item.getName())
                    .image(item == null ? null : item.getImage())
                    .build());
        }
        return List.copyOf(items);
    }

    private List<String> overviewWarnings(int rawLeftCount, int leftCount,
                                          int rawRightCount, int rightCount,
                                          List<MatchPair> pairs) {
        List<String> warnings = new ArrayList<>();
        if (rawLeftCount > leftCount) warnings.add("LEFT_MATCHES_EXCLUDED=" + (rawLeftCount - leftCount));
        if (rawRightCount > rightCount) warnings.add("RIGHT_MATCHES_EXCLUDED=" + (rawRightCount - rightCount));
        if (pairs.isEmpty()) warnings.add("NO_COMPARABLE_MATCH_PAIRS");
        else if (pairs.stream().noneMatch(MatchPair::deepComparisonAvailable)) {
            warnings.add("NO_SAME_HERO_PAIR");
        }
        return List.copyOf(warnings);
    }

    private static <T> List<T> safeList(List<T> values) {
        return values == null ? Collections.emptyList() : values;
    }
}
