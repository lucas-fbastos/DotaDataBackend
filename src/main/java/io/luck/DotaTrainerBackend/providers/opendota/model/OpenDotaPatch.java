package io.luck.DotaTrainerBackend.providers.opendota.model;

import lombok.Builder;

import java.time.Instant;

@Builder
public record OpenDotaPatch(
        String name,
        Instant date,
        Integer id
) {
}
