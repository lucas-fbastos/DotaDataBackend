package io.luck.AghsLabs.model;

import java.util.List;
import java.util.Map;
import tools.jackson.databind.JsonNode;

/** Structured match events and statistics exposed by the match details endpoint. */
public final class MatchDetailsData {
    private MatchDetailsData() {}

    public record TeamfightPlayer(
            Map<String, Map<String, Long>> deathsPos,
            Map<String, Long> abilityUses,
            Map<String, Map<String, Long>> abilityTargets,
            Map<String, Long> itemUses,
            Map<String, Long> killed,
            Integer deaths,
            Integer buybacks,
            Long damage,
            Long healing,
            Integer goldDelta,
            Integer xpDelta,
            Integer xpStart,
            Integer xpEnd
    ) {}

    public record Teamfight(
            Integer start,
            Integer end,
            Integer lastDeath,
            Integer deaths,
            List<TeamfightPlayer> players
    ) {}

    public record Pause(
            Integer time,
            Long duration
    ) {}

    public record Objective(
            Integer time,
            String type,
            JsonNode key,
            Integer slot,
            Integer playerSlot,
            Integer victimPlayerSlot,
            String unit,
            Integer team,
            Long value,
            Integer killer
    ) {}

    public record DraftTiming(
            Integer time,
            String type,
            Integer draftOrder,
            Boolean pick,
            Long heroId,
            Integer draftActiveTeam,
            Integer draftExtime0,
            Integer draftExtime1
    ) {}

    public record OdData(
            Boolean hasApi,
            Boolean hasGcData,
            Boolean hasParsed,
            Boolean hasArchive
    ) {}

    public record League(
            Long leagueId,
            JsonNode ticket,
            JsonNode banner,
            String tier,
            String name
    ) {}

    public record TeamInfo(
            Long teamId,
            String name,
            String tag,
            String logoUrl
    ) {}

    public record MaxHeroHit(
            Integer time,
            String type,
            String unit,
            String key,
            Long value,
            Integer slot,
            Integer playerSlot,
            Boolean max,
            String inflictor
    ) {}

    public record PurchaseLog(
            Integer time,
            String key,
            Integer charges
    ) {}

    public record KillLog(
            Integer time,
            String key,
            Boolean smoke
    ) {}

    public record DeathLog(
            Integer goldFed,
            Integer time,
            Integer goldLost,
            String key,
            Integer timeDead,
            Boolean goldFedShared
    ) {}

    public record BuybackLog(
            Integer time,
            String type,
            Integer slot,
            Integer playerSlot
    ) {}

    public record RuneLog(
            Integer time,
            String key
    ) {}

    public record ConnectionLog(
            Integer time,
            String event,
            Integer playerSlot,
            Integer slot,
            String type
    ) {}

    public record NeutralTokenLog(
            Integer time,
            String key,
            Integer playerSlot,
            Integer slot,
            String type
    ) {}

    public record NeutralItemHistory(
            String itemNeutral,
            Integer time,
            String itemNeutralEnhancement
    ) {}

    public record PermanentBuff(
            Integer permanentBuff,
            Integer stackCount,
            Integer grantTime
    ) {}

    public record Benchmark(
            Double raw,
            Double pct
    ) {}

    public record Benchmarks(
            Benchmark goldPerMin,
            Benchmark xpPerMin,
            Benchmark killsPerMin,
            Benchmark deathsPerMin,
            Benchmark assistsPerMin,
            Benchmark lastHitsPerMin,
            Benchmark deniesPerMin,
            Benchmark heroDamagePerMin,
            Benchmark heroHealingPerMin,
            Benchmark towerDamage
    ) {}

    public record PickBan(
            Boolean isPick,
            Long heroId,
            Integer team,
            Integer order
    ) {}
}
