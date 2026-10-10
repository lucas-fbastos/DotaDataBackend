package io.luck.AghsLabs.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.Set;

public record OpenDotaPlayerMatchDetails(
        @JsonProperty("match_id")
        Long matchId,
        Integer cluster,
        Long duration,
        @JsonProperty("game_mode")
        Integer gameMode,
        @JsonProperty("dire_score")
        Integer direScore,
        @JsonProperty("radiant_score")
        Integer radiantScore,
        @JsonProperty("radiant_win")
        Boolean radianWin,
        Set<OpenDotaMatchPlayer> players
) {


}


