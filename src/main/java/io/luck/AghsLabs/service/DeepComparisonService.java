package io.luck.AghsLabs.service;

import io.luck.AghsLabs.config.CacheConfig;
import io.luck.AghsLabs.exception.BadRequestException;
import io.luck.AghsLabs.exception.ResourceNotFoundException;
import io.luck.AghsLabs.model.dtos.ComparisonDtos.*;
import io.luck.AghsLabs.model.entity.Ability;
import io.luck.AghsLabs.model.entity.Item;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaMatchDetailsData.Teamfight;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaMatchDetailsData.TeamfightPlayer;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaMatchPlayer;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayerMatchDetails;
import io.luck.AghsLabs.providers.opendota.service.OpenDotaPlayerMatchDataService;
import io.luck.AghsLabs.repository.AbilityRepository;
import lombok.Builder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DeepComparisonService {
    private final OpenDotaPlayerMatchDataService matchDataService;
    private final ItemService itemService;
    private final AbilityRepository abilityRepository;

    public DeepComparisonService(OpenDotaPlayerMatchDataService matchDataService,
                                 ItemService itemService,
                                 AbilityRepository abilityRepository) {
        this.matchDataService = matchDataService;
        this.itemService = itemService;
        this.abilityRepository = abilityRepository;
    }

    @Cacheable(cacheNames = CacheConfig.DEEP_COMPARISONS,
            key = "#leftPlayerId + ':' + #leftMatchId + ':' + #leftSlot + ':' + "
                    + "#rightPlayerId + ':' + #rightMatchId + ':' + #rightSlot",
            unless = "#result == null || !#result.cacheable()")
    public DeepComparison compare(
            Long leftPlayerId,
            Long leftMatchId,
            Integer leftSlot,
            Long rightPlayerId,
            Long rightMatchId,
            Integer rightSlot
    ) {

        OpenDotaPlayerMatchDetails leftDetails = requireMatch(leftMatchId);
        OpenDotaPlayerMatchDetails rightDetails = requireMatch(rightMatchId);

        Participant left = requireParticipant(leftDetails, leftPlayerId, leftSlot, "left");
        Participant right = requireParticipant(rightDetails, rightPlayerId, rightSlot, "right");

        List<FarmCheckpoint> farmCheckpoints = farmCheckpoints(left.player(), right.player());

        TeamFightData leftTeamFights = teamFights(leftDetails, left.index());
        TeamFightData rightTeamFights = teamFights(rightDetails, right.index());


        boolean sameHero = left.player().heroId() != null
                && left.player().heroId().equals(right.player().heroId());

        boolean leftParsed  = isParsed(leftDetails);
        boolean rightParsed = isParsed(rightDetails);
        boolean leftFarmAvailable = hasFarmTimeline(left.player());
        boolean rightFarmAvailable = hasFarmTimeline(right.player());
        boolean leftPurchasesAvailable = left.player().purchaseLog() != null;
        boolean rightPurchasesAvailable = right.player().purchaseLog() != null;
        boolean leftAbilitiesAvailable = left.player().abilityUpgradesArr() != null;
        boolean rightAbilitiesAvailable = right.player().abilityUpgradesArr() != null;

        List<String> warnings = getWarnings(
                leftParsed,
                rightParsed,
                sameHero,
                leftFarmAvailable,
                rightFarmAvailable,
                farmCheckpoints.isEmpty(),
                leftPurchasesAvailable,
                rightPurchasesAvailable,
                leftAbilitiesAvailable,
                rightAbilitiesAvailable,
                leftTeamFights.available(),
                rightTeamFights.available(),
                leftTeamFights.partial(),
                rightTeamFights.partial()
        );

        if (!leftTeamFights.available()) warnings.add("LEFT_TEAMFIGHTS_UNAVAILABLE");
        if (!rightTeamFights.available()) warnings.add("RIGHT_TEAMFIGHTS_UNAVAILABLE");
        if (leftTeamFights.partial()) warnings.add("LEFT_TEAMFIGHT_PLAYER_DATA_PARTIAL");
        if (rightTeamFights.partial()) warnings.add("RIGHT_TEAMFIGHT_PLAYER_DATA_PARTIAL");

        Set<Long> itemIds = java.util.stream.Stream.concat(
                        left.player().getItems().stream(), right.player().getItems().stream())
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());
        Map<Long, Item> itemsById = itemIds.isEmpty() ? Map.of() : itemService.getAllById(itemIds).stream()
                .collect(Collectors.toMap(Item::getItemId, Function.identity()));

        Set<Long> abilityIds = java.util.stream.Stream.concat(
                        copyOrEmpty(left.player().abilityUpgradesArr()).stream(),
                        copyOrEmpty(right.player().abilityUpgradesArr()).stream())
                .filter(Objects::nonNull)
                .map(Integer::longValue)
                .collect(Collectors.toSet());
        Map<Long, Ability> abilitiesById = abilityIds.isEmpty() ? Map.of()
                : abilityRepository.findAllById(abilityIds).stream()
                        .collect(Collectors.toMap(Ability::getId, Function.identity()));

        boolean completeData = leftParsed && rightParsed
                && leftFarmAvailable && rightFarmAvailable && !farmCheckpoints.isEmpty()
                && leftPurchasesAvailable && rightPurchasesAvailable
                && leftAbilitiesAvailable && rightAbilitiesAvailable
                && leftTeamFights.available() && rightTeamFights.available()
                && !leftTeamFights.partial() && !rightTeamFights.partial();

        return DeepComparison.builder()
                .deltaDefinition("LEFT_MINUS_RIGHT")
                .sameHero(sameHero)
                .deepComparisonAvailable(sameHero)
                .completeData(completeData)
                .left(playerSummary(leftPlayerId, leftDetails, left.player()))
                .right(playerSummary(rightPlayerId, rightDetails, right.player()))
                .summaryDelta(summaryDelta(left.player(), right.player()))
                .farmCheckpoints(farmCheckpoints)
                .itemComparison(itemComparison(left.player(), right.player(), itemsById))
                .abilityComparison(AbilityComparison.builder()
                        .leftOrder(abilityOrder(left.player().abilityUpgradesArr(), abilitiesById))
                        .rightOrder(abilityOrder(right.player().abilityUpgradesArr(), abilitiesById))
                        .build())
                .teamfightComparison(TeamFightComparison.builder()
                        .left(leftTeamFights.aggregate())
                        .right(rightTeamFights.aggregate())
                        .delta(teamFightDelta(leftTeamFights.aggregate(), rightTeamFights.aggregate()))
                        .leftTimeline(leftTeamFights.timeline())
                        .rightTimeline(rightTeamFights.timeline())
                        .build())
                .availabilityWarnings(List.copyOf(warnings))
                .build();
    }

    private List<AbilitySummary> abilityOrder(List<Integer> abilityIds, Map<Long, Ability> abilitiesById) {
        return copyOrEmpty(abilityIds).stream()
                .map(id -> id == null ? null : abilitiesById.get(id.longValue()))
                .map(ability -> ability == null
                        ? new AbilitySummary(null, null)
                        : new AbilitySummary(ability.getName(), ability.getIcon()))
                .toList();
    }

    private List<String> getWarnings(
            boolean leftParsed,
            boolean rightParsed,
            boolean sameHero,
            boolean leftFarmAvailable,
            boolean rightFarmAvailable,
            boolean emptyFarmCheckpoints,
            boolean leftPurchasesAvailable,
            boolean rightPurchasesAvailable,
            boolean leftAbilitiesAvailable,
            boolean rightAbilitiesAvailable,
            boolean leftTeamFightsAvailable,
            boolean rightTeamFightsAvailable,
            boolean leftTeamFightsPartial,
            boolean rightTeamFightsPartial
    ) {
        List<String> warnings = new ArrayList<>();

        if (!leftParsed) warnings.add("LEFT_MATCH_NOT_PARSED");
        if (!rightParsed) warnings.add("RIGHT_MATCH_NOT_PARSED");
        if (!sameHero) warnings.add("HERO_MISMATCH");
        if (!leftFarmAvailable) warnings.add("LEFT_FARM_TIMELINE_UNAVAILABLE");
        if (!rightFarmAvailable) warnings.add("RIGHT_FARM_TIMELINE_UNAVAILABLE");

        if (leftFarmAvailable && rightFarmAvailable && emptyFarmCheckpoints) {
            warnings.add("NO_SHARED_FARM_CHECKPOINTS");
        }

        if (!leftPurchasesAvailable) warnings.add("LEFT_PURCHASE_TIMINGS_UNAVAILABLE");
        if (!rightPurchasesAvailable) warnings.add("RIGHT_PURCHASE_TIMINGS_UNAVAILABLE");

        if (!leftAbilitiesAvailable) warnings.add("LEFT_ABILITY_ORDER_UNAVAILABLE");
        if (!rightAbilitiesAvailable) warnings.add("RIGHT_ABILITY_ORDER_UNAVAILABLE");


        if (!leftTeamFightsAvailable) warnings.add("LEFT_TEAMFIGHTS_UNAVAILABLE");
        if (!rightTeamFightsAvailable) warnings.add("RIGHT_TEAMFIGHTS_UNAVAILABLE");
        if (leftTeamFightsPartial) warnings.add("LEFT_TEAMFIGHT_PLAYER_DATA_PARTIAL");
        if (rightTeamFightsPartial) warnings.add("RIGHT_TEAMFIGHT_PLAYER_DATA_PARTIAL");

        return warnings;
    }

    private OpenDotaPlayerMatchDetails requireMatch(Long matchId) {
        OpenDotaPlayerMatchDetails details = matchDataService.getMatchDetails(matchId);
        if (details == null) throw new ResourceNotFoundException("Match " + matchId + " was not found");
        if (details.matchId() != null && !matchId.equals(details.matchId())) {
            throw new BadRequestException("OpenDota returned an unexpected match for " + matchId);
        }
        return details;
    }

    private Participant requireParticipant(OpenDotaPlayerMatchDetails details, Long playerId,
                                           Integer playerSlot, String side) {
        if (details.players() == null) {
            throw new ResourceNotFoundException("The " + side + " match has no player data");
        }
        for (int index = 0; index < details.players().size(); index++) {
            OpenDotaMatchPlayer player = details.players().get(index);
            if (player != null && playerId.equals(player.accountId()) && playerSlot.equals(player.playerSlot())) {
                return Participant.builder()
                        .player(player)
                        .index(index)
                        .build();
            }
        }
        throw new BadRequestException("Player " + playerId + " with slot " + playerSlot
                + " did not participate in the " + side + " match");
    }

    private boolean isParsed(OpenDotaPlayerMatchDetails details) {
        if (details.odData() != null && details.odData().hasParsed() != null) {
            return details.odData().hasParsed();
        }
        return details.version() != null;
    }

    private DeepPlayerSummary playerSummary(Long playerId, OpenDotaPlayerMatchDetails details,
                                            OpenDotaMatchPlayer player) {
        Long duration = player.duration() != null ? player.duration() : details.duration();
        Long startTime = player.startTime() != null ? player.startTime() : details.startTime();
        Integer patch = player.patch() != null ? player.patch() : details.patch();
        return DeepPlayerSummary.builder()
                .playerId(playerId)
                .matchId(details.matchId())
                .playerSlot(player.playerSlot())
                .heroId(player.heroId())
                .won(player.playerWon())
                .durationSeconds(duration)
                .startTime(startTime)
                .patch(patch)
                .laneRole(player.laneRole())
                .level(player.level())
                .kills(player.kills())
                .deaths(player.deaths())
                .assists(player.assists())
                .lastHits(player.lastHits())
                .denies(player.denies())
                .goldPerMinute(player.goldPerMin())
                .xpPerMinute(player.xpPerMin())
                .netWorth(player.netWorth())
                .heroDamage(player.heroDamage())
                .towerDamage(player.towerDamage())
                .heroHealing(player.heroHealing())
                .teamFightParticipation(player.teamfightParticipation())
                .build();
    }

    private SummaryDelta summaryDelta(OpenDotaMatchPlayer left, OpenDotaMatchPlayer right) {
        return SummaryDelta.builder()
                .level(difference(left.level(), right.level()))
                .kills(difference(left.kills(), right.kills()))
                .deaths(difference(left.deaths(), right.deaths()))
                .assists(difference(left.assists(), right.assists()))
                .lastHits(difference(left.lastHits(), right.lastHits()))
                .denies(difference(left.denies(), right.denies()))
                .goldPerMinute(difference(left.goldPerMin(), right.goldPerMin()))
                .xpPerMinute(difference(left.xpPerMin(), right.xpPerMin()))
                .netWorth(difference(left.netWorth(), right.netWorth()))
                .heroDamage(difference(left.heroDamage(), right.heroDamage()))
                .towerDamage(difference(left.towerDamage(), right.towerDamage()))
                .heroHealing(difference(left.heroHealing(), right.heroHealing()))
                .teamFightParticipation(difference(left.teamfightParticipation(), right.teamfightParticipation()))
                .build();
    }

    private boolean hasFarmTimeline(OpenDotaMatchPlayer player) {
        return hasValues(player.goldT()) || hasValues(player.lhT()) || hasValues(player.dnT())
                || hasValues(player.xpT()) || hasValues(player.netWorthT());
    }

    private List<FarmCheckpoint> farmCheckpoints(OpenDotaMatchPlayer left, OpenDotaMatchPlayer right) {
        Map<Integer, FarmValues> leftByTime = farmByTime(left);
        Map<Integer, FarmValues> rightByTime = farmByTime(right);
        TreeSet<Integer> commonTimes = new TreeSet<>(leftByTime.keySet());
        commonTimes.retainAll(rightByTime.keySet());
        return commonTimes.stream()
                .map(time -> {
                    FarmValues leftValue = leftByTime.get(time);
                    FarmValues rightValue = rightByTime.get(time);
                    return FarmCheckpoint.builder()
                            .timeSeconds(time)
                            .left(leftValue)
                            .right(rightValue)
                            .delta(FarmValues.builder()
                                    .gold(difference(leftValue.gold(), rightValue.gold()))
                                    .lastHits(difference(leftValue.lastHits(), rightValue.lastHits()))
                                    .denies(difference(leftValue.denies(), rightValue.denies()))
                                    .xp(difference(leftValue.xp(), rightValue.xp()))
                                    .netWorth(difference(leftValue.netWorth(), rightValue.netWorth()))
                                    .build())
                            .build();
                })
                .toList();
    }

    private Map<Integer, FarmValues> farmByTime(OpenDotaMatchPlayer player) {
        int size = maxSize(player.times(), player.goldT(), player.lhT(), player.dnT(),
                player.xpT(), player.netWorthT());
        Map<Integer, FarmValues> result = new HashMap<>();
        for (int index = 0; index < size; index++) {
            Integer time = valueAt(player.times(), index);
            if (time == null) time = index * 60;
            FarmValues values = FarmValues.builder()
                    .gold(valueAt(player.goldT(), index))
                    .lastHits(valueAt(player.lhT(), index))
                    .denies(valueAt(player.dnT(), index))
                    .xp(valueAt(player.xpT(), index))
                    .netWorth(valueAt(player.netWorthT(), index))
                    .build();
            if (values.gold() != null || values.lastHits() != null || values.denies() != null
                    || values.xp() != null || values.netWorth() != null) {
                result.put(time, values);
            }
        }
        return result;
    }

    private int maxSize(List<?>... lists) {
        int maximum = 0;
        for (List<?> list : lists) maximum = Math.max(maximum, list == null ? 0 : list.size());
        return maximum;
    }

    private ItemComparison itemComparison(OpenDotaMatchPlayer left, OpenDotaMatchPlayer right,
                                          Map<Long, Item> itemsById) {
        Map<String, Integer> leftPurchases = firstPurchaseTimes(left);
        Map<String, Integer> rightPurchases = firstPurchaseTimes(right);
        TreeSet<String> itemKeys = new TreeSet<>(leftPurchases.keySet());
        itemKeys.addAll(rightPurchases.keySet());
        List<PurchaseTiming> timings = itemKeys.stream()
                .map(key -> PurchaseTiming.builder()
                        .itemKey(key)
                        .leftTimeSeconds(leftPurchases.get(key))
                        .rightTimeSeconds(rightPurchases.get(key))
                        .deltaSeconds(difference(leftPurchases.get(key), rightPurchases.get(key)))
                        .build())
                .toList();
        return ItemComparison.builder()
                .leftFinalItems(ComparisonService.itemSummaries(left.getItems(), itemsById))
                .rightFinalItems(ComparisonService.itemSummaries(right.getItems(), itemsById))
                .purchaseTimings(timings)
                .build();
    }

    private Map<String, Integer> firstPurchaseTimes(OpenDotaMatchPlayer player) {
        if (player.purchaseLog() == null) return Map.of();
        Map<String, Integer> firstPurchase = new HashMap<>();
        player.purchaseLog().stream()
                .filter(Objects::nonNull)
                .filter(purchase -> purchase.key() != null && purchase.time() != null)
                .forEach(purchase -> firstPurchase.merge(purchase.key(), purchase.time(), Math::min));
        return firstPurchase;
    }

    private TeamFightData teamFights(OpenDotaPlayerMatchDetails details, int playerIndex) {
        if (details.teamfights() == null) return TeamFightData.unavailable();
        List<TeamFightEvent> timeline = new ArrayList<>();
        boolean partial = false;
        for (Teamfight fight : details.teamfights()) {
            if (fight == null) continue;
            TeamfightPlayer player = null;
            if (fight.players() != null && playerIndex < fight.players().size()) {
                player = fight.players().get(playerIndex);
            }
            if (player == null) partial = true;
            timeline.add(TeamFightEvent.builder()
                    .startSeconds(fight.start())
                    .endSeconds(fight.end())
                    .lastDeathSeconds(fight.lastDeath())
                    .deaths(player == null ? null : player.deaths())
                    .buybacks(player == null ? null : player.buybacks())
                    .damage(player == null ? null : player.damage())
                    .healing(player == null ? null : player.healing())
                    .goldDelta(player == null ? null : player.goldDelta())
                    .xpDelta(player == null ? null : player.xpDelta())
                    .build());
        }
        List<TeamFightEvent> immutableTimeline = List.copyOf(timeline);
        return TeamFightData.builder()
                .aggregate(aggregate(immutableTimeline))
                .timeline(immutableTimeline)
                .available(true)
                .partial(partial)
                .build();
    }

    private TeamFightAggregate aggregate(List<TeamFightEvent> timeline) {
        return TeamFightAggregate.builder()
                .fightCount(timeline.size())
                .damage(sumLong(timeline.stream().map(TeamFightEvent::damage).toList()))
                .healing(sumLong(timeline.stream().map(TeamFightEvent::healing).toList()))
                .deaths(sumInteger(timeline.stream().map(TeamFightEvent::deaths).toList()))
                .buybacks(sumInteger(timeline.stream().map(TeamFightEvent::buybacks).toList()))
                .goldDelta(sumIntegerAsLong(timeline.stream().map(TeamFightEvent::goldDelta).toList()))
                .xpDelta(sumIntegerAsLong(timeline.stream().map(TeamFightEvent::xpDelta).toList()))
                .firstFightStartSeconds(timeline.stream().map(TeamFightEvent::startSeconds)
                        .filter(Objects::nonNull).findFirst().orElse(null))
                .build();
    }

    private TeamFightAggregate teamFightDelta(
            TeamFightAggregate left,
            TeamFightAggregate right
    ) {
        return TeamFightAggregate.builder()
                .fightCount(difference(left.fightCount(), right.fightCount()))
                .damage(difference(left.damage(), right.damage()))
                .healing(difference(left.healing(), right.healing()))
                .deaths(difference(left.deaths(), right.deaths()))
                .buybacks(difference(left.buybacks(), right.buybacks()))
                .goldDelta(difference(left.goldDelta(), right.goldDelta()))
                .xpDelta(difference(left.xpDelta(), right.xpDelta()))
                .firstFightStartSeconds(difference(left.firstFightStartSeconds(), right.firstFightStartSeconds()))
                .build();
    }

    private Long sumLong(List<Long> values) {
        if (values.stream().allMatch(Objects::isNull)) return null;
        return values.stream().filter(Objects::nonNull).mapToLong(Long::longValue).sum();
    }

    private Integer sumInteger(List<Integer> values) {
        if (values.stream().allMatch(Objects::isNull)) return null;
        return values.stream().filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
    }

    private Long sumIntegerAsLong(List<Integer> values) {
        if (values.stream().allMatch(Objects::isNull)) return null;
        return values.stream().filter(Objects::nonNull).mapToLong(Integer::longValue).sum();
    }

    private static boolean hasValues(List<?> values) {
        return values != null && !values.isEmpty();
    }

    private static <T> T valueAt(List<T> values, int index) {
        return values != null && index < values.size() ? values.get(index) : null;
    }

    private static Integer difference(Integer left, Integer right) {
        return left == null || right == null ? null : left - right;
    }

    private static Long difference(Long left, Long right) {
        return left == null || right == null ? null : left - right;
    }

    private static Double difference(Double left, Double right) {
        return left == null || right == null ? null : left - right;
    }

    private static <T> List<T> copyOrEmpty(List<T> values) {
        return values == null ? List.of() : List.copyOf(values);
    }

    @Builder
    private record Participant(OpenDotaMatchPlayer player, int index) {
    }

    @Builder
    private record TeamFightData(TeamFightAggregate aggregate, List<TeamFightEvent> timeline,
                                 boolean available, boolean partial) {
        private static TeamFightData unavailable() {
            return TeamFightData.builder()
                    .aggregate(TeamFightAggregate.builder().build())
                    .timeline(Collections.emptyList())
                    .available(false)
                    .partial(false)
                    .build();
        }
    }
}
