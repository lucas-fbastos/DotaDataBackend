package io.luck.AghsLabs.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record OpenDotaPlayer(

        @JsonProperty("rank_tier")
        Integer rankTier,

        @JsonProperty("leaderboard_rank")
        Integer leaderboardRank,

        @JsonProperty("computed_mmr")
        Integer computedMmr,

        @JsonProperty("computed_mmr_turbo")
        Integer computedMmrTurbo,

        List<Alias> aliases,

        Profile profile

) {
    public record Alias(

            @JsonProperty("personaname")
            String personaName,

            @JsonProperty("name_since")
            String nameSince

    ) {}

    public record Profile(

            @JsonProperty("account_id")
            Long accountId,

            @JsonProperty("personaname")
            String personaName,
            String name,
            Boolean plus,
            Integer cheese,
            @JsonProperty("steamid")
            String steamId,
            String avatar,
            @JsonProperty("avatarmedium")
            String avatarMedium,
            @JsonProperty("avatarfull")
            String avatarFull,
            @JsonProperty("profileurl")
            String profileUrl,

            @JsonProperty("last_login")
            String lastLogin,

            @JsonProperty("loccountrycode")
            String locCountryCode,

            @JsonProperty("is_contributor")
            Boolean isContributor,

            @JsonProperty("is_subscriber")
            Boolean isSubscriber

    ) {}
}