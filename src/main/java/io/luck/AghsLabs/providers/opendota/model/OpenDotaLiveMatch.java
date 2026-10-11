package io.luck.AghsLabs.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

/** Subset of the fields returned by OpenDota's /live endpoint. */
@Builder
public record OpenDotaLiveMatch(
        @JsonProperty("match_id") Long matchId,
        @JsonProperty("game_time") Integer gameTime,
        @JsonProperty("average_mmr") Integer averageMmr,
        @JsonProperty("radiant_score") Integer radiantScore,
        @JsonProperty("dire_score") Integer direScore,
        List<OpenDotaLivePlayer> players
) {
    @Builder
    public record OpenDotaLivePlayer(
            @JsonProperty("hero_id") Long heroId,
            Integer team
    ) {}
}
