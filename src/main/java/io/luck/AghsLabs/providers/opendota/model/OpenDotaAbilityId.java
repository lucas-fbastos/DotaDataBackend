package io.luck.AghsLabs.providers.opendota.model;

import lombok.Builder;

@Builder
public record OpenDotaAbilityId(
        Long id,
        String name
) {
}
