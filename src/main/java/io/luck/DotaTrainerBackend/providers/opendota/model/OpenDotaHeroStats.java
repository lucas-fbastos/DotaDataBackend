package io.luck.DotaTrainerBackend.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenDotaHeroStats(
        @JsonProperty("hero_id")
        Long heroId,

        @JsonProperty("last_played")
        Long lastPlayed,

        Long games,

        Long win,

        @JsonProperty("with_games")
        Long withGames,

        @JsonProperty("with_win")
        Long winWith,

        @JsonProperty("against_games")
        Long againstGames,

        @JsonProperty("against_win")
        Long againstWin
) {
}
