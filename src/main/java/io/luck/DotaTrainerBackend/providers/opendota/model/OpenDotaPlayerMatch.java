package io.luck.DotaTrainerBackend.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenDotaPlayerMatch(
        @JsonProperty("match_id")
        Long matchId,

        @JsonProperty("player_slot")
        Integer playerSlot,

        @JsonProperty("radiant_win")
        Boolean radiantWin,

        Integer duration,

        @JsonProperty("game_mode")
        Integer gameMode,

        @JsonProperty("lobby_type")
        Integer lobbyType,

        @JsonProperty("hero_id")
        Integer heroId,

        @JsonProperty("start_time")
        Long startTime,

        Integer version,

        Integer kills,

        Integer deaths,

        Integer assists,

        Integer skill,

        @JsonProperty("average_rank")
        Integer averageRank,

        @JsonProperty("leaver_status")
        Integer leaverStatus,

        @JsonProperty("party_size")
        Integer partySize,

        @JsonProperty("hero_variant")
        Integer heroVariant
) {
}
