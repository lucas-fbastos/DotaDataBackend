package io.luck.AghsLabs.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import tools.jackson.databind.JsonNode;

/** Structured match events and statistics returned by OpenDota. */
public final class OpenDotaMatchDetailsData {
    private OpenDotaMatchDetailsData() {}

    public record TeamfightPlayer(
            @JsonProperty("deaths_pos") Map<String, Map<String, Long>> deathsPos,
            @JsonProperty("ability_uses") Map<String, Long> abilityUses,
            @JsonProperty("ability_targets") Map<String, Map<String, Long>> abilityTargets,
            @JsonProperty("item_uses") Map<String, Long> itemUses,
            Map<String, Long> killed,
            Integer deaths,
            Integer buybacks,
            Long damage,
            Long healing,
            @JsonProperty("gold_delta") Integer goldDelta,
            @JsonProperty("xp_delta") Integer xpDelta,
            @JsonProperty("xp_start") Integer xpStart,
            @JsonProperty("xp_end") Integer xpEnd
    ) {}

    public record Teamfight(
            Integer start,
            Integer end,
            @JsonProperty("last_death") Integer lastDeath,
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
            @JsonProperty("player_slot") Integer playerSlot,
            @JsonProperty("victim_player_slot") Integer victimPlayerSlot,
            String unit,
            Integer team,
            Long value,
            Integer killer
    ) {}

    public record Chat(
            Integer time,
            String type,
            String key,
            Integer slot,
            @JsonProperty("player_slot") Integer playerSlot
    ) {}

    public record DraftTiming(
            Integer time,
            String type,
            @JsonProperty("draft_order") Integer draftOrder,
            Boolean pick,
            @JsonProperty("hero_id") Long heroId,
            @JsonProperty("draft_active_team") Integer draftActiveTeam,
            @JsonProperty("draft_extime0") Integer draftExtime0,
            @JsonProperty("draft_extime1") Integer draftExtime1
    ) {}

    public record OdData(
            @JsonProperty("has_api") Boolean hasApi,
            @JsonProperty("has_gcdata") Boolean hasGcData,
            @JsonProperty("has_parsed") Boolean hasParsed,
            @JsonProperty("has_archive") Boolean hasArchive
    ) {}

    public record League(
            @JsonProperty("leagueid") Long leagueId,
            JsonNode ticket,
            JsonNode banner,
            String tier,
            String name
    ) {}

    public record TeamInfo(
            @JsonProperty("team_id") Long teamId,
            String name,
            String tag,
            @JsonProperty("logo_url") String logoUrl
    ) {}

    public record MaxHeroHit(
            Integer time,
            String type,
            String unit,
            String key,
            Long value,
            Integer slot,
            @JsonProperty("player_slot") Integer playerSlot,
            Boolean max,
            String inflictor
    ) {}

    public record WardLog(
            Integer time,
            String type,
            String key,
            Integer slot,
            @JsonProperty("player_slot") Integer playerSlot,
            Double x,
            Double y,
            Double z,
            @JsonProperty("entityleft") Boolean entityLeft,
            @JsonProperty("ehandle") Integer entityHandle,
            @JsonProperty("attackername") String attackerName
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
            @JsonProperty("gold_fed") Integer goldFed,
            Integer time,
            @JsonProperty("gold_lost") Integer goldLost,
            String key,
            @JsonProperty("time_dead") Integer timeDead,
            @JsonProperty("gold_fed_shared") Boolean goldFedShared
    ) {}

    public record BuybackLog(
            Integer time,
            String type,
            Integer slot,
            @JsonProperty("player_slot") Integer playerSlot
    ) {}

    public record RuneLog(
            Integer time,
            String key
    ) {}

    public record ConnectionLog(
            Integer time,
            String event,
            @JsonProperty("player_slot") Integer playerSlot,
            Integer slot,
            String type
    ) {}

    public record NeutralTokenLog(
            Integer time,
            String key,
            @JsonProperty("player_slot") Integer playerSlot,
            Integer slot,
            String type
    ) {}

    public record NeutralItemHistory(
            @JsonProperty("item_neutral") String itemNeutral,
            Integer time,
            @JsonProperty("item_neutral_enhancement") String itemNeutralEnhancement
    ) {}

    public record PermanentBuff(
            @JsonProperty("permanent_buff") Integer permanentBuff,
            @JsonProperty("stack_count") Integer stackCount,
            @JsonProperty("grant_time") Integer grantTime
    ) {}

    public record Benchmark(
            Double raw,
            Double pct
    ) {}

    public record Benchmarks(
            @JsonProperty("gold_per_min") Benchmark goldPerMin,
            @JsonProperty("xp_per_min") Benchmark xpPerMin,
            @JsonProperty("kills_per_min") Benchmark killsPerMin,
            @JsonProperty("deaths_per_min") Benchmark deathsPerMin,
            @JsonProperty("assists_per_min") Benchmark assistsPerMin,
            @JsonProperty("last_hits_per_min") Benchmark lastHitsPerMin,
            @JsonProperty("denies_per_min") Benchmark deniesPerMin,
            @JsonProperty("hero_damage_per_min") Benchmark heroDamagePerMin,
            @JsonProperty("hero_healing_per_min") Benchmark heroHealingPerMin,
            @JsonProperty("tower_damage") Benchmark towerDamage
    ) {}

    public record PickBan(
            @JsonProperty("is_pick") Boolean isPick,
            @JsonProperty("hero_id") Long heroId,
            Integer team,
            Integer order
    ) {}
}
