package io.luck.AghsLabs.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.luck.AghsLabs.model.Enums.Faction;
import lombok.Builder;

import java.util.Arrays;
import java.util.List;

@Builder
public record OpenDotaPlayerMatchCandidate(
        @JsonProperty("match_id") Long matchId,
        @JsonProperty("account_id") Long accountId,
        @JsonProperty("player_slot") Integer playerSlot,
        @JsonProperty("radiant_win") Boolean radiantWin,
        Integer duration,
        @JsonProperty("game_mode") Integer gameMode,
        @JsonProperty("lobby_type") Integer lobbyType,
        @JsonProperty("hero_id") Long heroId,
        @JsonProperty("start_time") Long startTime,
        Integer version,
        Integer patch,
        Integer kills,
        Integer deaths,
        Integer assists,
        @JsonProperty("leaver_status") Integer leaverStatus,
        @JsonProperty("last_hits") Integer lastHits,
        Integer denies,
        @JsonProperty("gold_per_min") Integer goldPerMinute,
        @JsonProperty("xp_per_min") Integer xpPerMinute,
        @JsonProperty("net_worth") Long netWorth,
        @JsonProperty("hero_damage") Long heroDamage,
        @JsonProperty("tower_damage") Long towerDamage,
        @JsonProperty("hero_healing") Long heroHealing,
        Integer lane,
        @JsonProperty("lane_role") Integer laneRole,
        @JsonProperty("item_0") Long item0,
        @JsonProperty("item_1") Long item1,
        @JsonProperty("item_2") Long item2,
        @JsonProperty("item_3") Long item3,
        @JsonProperty("item_4") Long item4,
        @JsonProperty("item_5") Long item5
) {
    public List<Long> items() {
        return Arrays.asList(item0, item1, item2, item3, item4, item5);
    }

    public Boolean playerWon() {
        if (radiantWin == null || playerSlot == null) return null;
        return Faction.fromPlayerSlot(playerSlot).won(radiantWin);
    }
}
