package io.luck.DotaTrainerBackend.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenDotaProPlayer(
        @JsonProperty("account_id")
        Long accountId,

        @JsonProperty("steamid")
        String steamId,

        String avatar,

        @JsonProperty("avatarmedium")
        String avatarMedium,

        @JsonProperty("avatarfull")
        String avatarFull,

        @JsonProperty("profileurl")
        String profileUrl,

        @JsonProperty("personaname")
        String personaName,

        @JsonProperty("last_login")
        String lastLogin,

        @JsonProperty("full_history_time")
        String fullHistoryTime,

        Integer cheese,

        @JsonProperty("fh_unavailable")
        Boolean fullHistoryUnavailable,

        @JsonProperty("loccountrycode")
        String locCountryCode,

        String name,

        @JsonProperty("country_code")
        String countryCode,

        @JsonProperty("fantasy_role")
        Integer fantasyRole,

        @JsonProperty("team_id")
        Long teamId,

        @JsonProperty("team_name")
        String teamName,

        @JsonProperty("team_tag")
        String teamTag,

        @JsonProperty("is_locked")
        Boolean isLocked,

        @JsonProperty("is_pro")
        Boolean isPro,

        @JsonProperty("locked_until")
        Long lockedUntil,

        @JsonProperty("computed_mmr")
        Integer computedMmr
) {
}
