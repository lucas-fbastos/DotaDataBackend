package io.luck.AghsLabs.model.dtos;

import lombok.Builder;

import java.util.List;

public final class ComparisonDtos {
    private ComparisonDtos() {}

    @Builder
    public record ComparisonOverview(
            ComparisonPlayer leftPlayer,
            ComparisonPlayer rightPlayer,
            int leftCandidateCount,
            int rightCandidateCount,
            List<HeroAggregate> leftHeroAggregates,
            List<HeroAggregate> rightHeroAggregates,
            List<MatchPair> pairs,
            List<String> warnings
    ) { }

    @Builder
    public record ComparisonPlayer(
            Long accountId,
            String personaName,
            String avatarFull,
            Integer rankTier,
            Integer leaderboardRank,
            boolean pro,
            String teamName,
            String teamTag
    ) {}

    @Builder
    public record HeroAggregate(
            Long heroId,
            String heroName,
            String heroImage,
            long games,
            long wins,
            double winRate
    ) {}

    @Builder
    public record MatchPair(
            OverviewMatch left,
            OverviewMatch right,
            long qualityScore,
            CompatibilityTier compatibilityTier,
            List<CompatibilityReason> compatibilityReasons,
            boolean deepComparisonAvailable
    ) {}

    public enum CompatibilityTier {
        OPTIMAL,
        STRONG,
        COMPATIBLE,
        WEAK
    }

    public enum CompatibilityReason {
        SAME_HERO,
        SAME_PATCH,
        SAME_LANE_ROLE,
        SAME_GAME_MODE,
        SAME_LOBBY_TYPE,
        SIMILAR_DURATION,
        CLOSE_IN_TIME
    }

    @Builder
    public record OverviewMatch(
            Long matchId,
            Integer playerSlot,
            Long heroId,
            String heroName,
            String heroImage,
            Boolean won,
            Integer durationSeconds,
            Long startTime,
            Integer patch,
            Integer laneRole,
            Integer gameMode,
            Integer lobbyType,
            Integer kills,
            Integer deaths,
            Integer assists,
            Integer lastHits,
            Integer denies,
            Integer goldPerMinute,
            Integer xpPerMinute,
            Long netWorth,
            List<ItemSummary> items
    ) {}

    @Builder
    public record ItemSummary(
            int slot,
            Long itemId,
            String name,
            String image
    ) {}

    @Builder
    public record DeepComparison(
            String deltaDefinition,
            boolean sameHero,
            boolean deepComparisonAvailable,
            boolean completeData,
            DeepPlayerSummary left,
            DeepPlayerSummary right,
            SummaryDelta summaryDelta,
            List<FarmCheckpoint> farmCheckpoints,
            ItemComparison itemComparison,
            AbilityComparison abilityComparison,
            TeamFightComparison teamfightComparison,
            List<String> availabilityWarnings
    ) {
        public boolean cacheable() {
            return completeData;
        }
    }

    @Builder
    public record DeepPlayerSummary(
            Long playerId,
            Long matchId,
            Integer playerSlot,
            Long heroId,
            Boolean won,
            Long durationSeconds,
            Long startTime,
            Integer patch,
            Integer laneRole,
            Integer level,
            Integer kills,
            Integer deaths,
            Integer assists,
            Integer lastHits,
            Integer denies,
            Integer goldPerMinute,
            Integer xpPerMinute,
            Long netWorth,
            Long heroDamage,
            Long towerDamage,
            Long heroHealing,
            Double teamFightParticipation
    ) {}

    @Builder
    public record SummaryDelta(
            Integer level,
            Integer kills,
            Integer deaths,
            Integer assists,
            Integer lastHits,
            Integer denies,
            Integer goldPerMinute,
            Integer xpPerMinute,
            Long netWorth,
            Long heroDamage,
            Long towerDamage,
            Long heroHealing,
            Double teamFightParticipation
    ) {}

    @Builder
    public record FarmCheckpoint(
            Integer timeSeconds,
            FarmValues left,
            FarmValues right,
            FarmValues delta
    ) {}

    @Builder
    public record FarmValues(
            Long gold,
            Long lastHits,
            Long denies,
            Long xp,
            Long netWorth
    ) {}

    @Builder
    public record ItemComparison(
            List<ItemSummary> leftFinalItems,
            List<ItemSummary> rightFinalItems,
            List<PurchaseTiming> purchaseTimings
    ) {}

    @Builder
    public record PurchaseTiming(
            String itemKey,
            Integer leftTimeSeconds,
            Integer rightTimeSeconds,
            Integer deltaSeconds
    ) {}

    @Builder
    public record AbilityComparison(
            List<AbilitySummary> leftOrder,
            List<AbilitySummary> rightOrder
    ) {}

    @Builder
    public record AbilitySummary(
            String name,
            String image
    ) {}

    @Builder
    public record TeamFightComparison(
            TeamFightAggregate left,
            TeamFightAggregate right,
            TeamFightAggregate delta,
            List<TeamFightEvent> leftTimeline,
            List<TeamFightEvent> rightTimeline
    ) {}

    @Builder
    public record TeamFightAggregate(
            Integer fightCount,
            Long damage,
            Long healing,
            Integer deaths,
            Integer buybacks,
            Long goldDelta,
            Long xpDelta,
            Integer firstFightStartSeconds
    ) {}

    @Builder
    public record TeamFightEvent(
            Integer startSeconds,
            Integer endSeconds,
            Integer lastDeathSeconds,
            Integer deaths,
            Integer buybacks,
            Long damage,
            Long healing,
            Integer goldDelta,
            Integer xpDelta
    ) {}
}
