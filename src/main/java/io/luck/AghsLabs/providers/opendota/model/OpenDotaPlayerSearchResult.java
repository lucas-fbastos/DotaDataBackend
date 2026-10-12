package io.luck.AghsLabs.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record OpenDotaPlayerSearchResult(
        @JsonProperty("account_id") Long accountId,
        @JsonProperty("personaname") String personaName,
        @JsonProperty("avatarfull") String avatarFull,
        @JsonProperty("last_match_time") String lastMatchTime,
        Double similarity
) {}
