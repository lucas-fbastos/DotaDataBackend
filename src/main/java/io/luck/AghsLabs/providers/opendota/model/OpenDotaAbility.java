package io.luck.AghsLabs.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record OpenDotaAbility(
        @JsonProperty("dname")
        String displayName,

        String img
) {
}
