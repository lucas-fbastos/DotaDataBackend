package io.luck.AghsLabs.model;

import lombok.Builder;

import java.time.Duration;
import java.util.List;

@Builder
public record LiveMatch(
        Long matchId,
        Duration duration,
        Integer radiantScore,
        Integer direScore,
        Integer averageMmr,
        List<String> radiantHeroImages,
        List<String> direHeroImages
) {
}
