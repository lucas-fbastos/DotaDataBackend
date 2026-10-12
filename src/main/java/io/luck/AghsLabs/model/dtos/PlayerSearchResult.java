package io.luck.AghsLabs.model.dtos;

import lombok.Builder;

@Builder
public record PlayerSearchResult(
        Long accountId,
        String personaName,
        String avatarFull,
        String lastMatchTime,
        Double similarity,
        boolean pro,
        String teamName,
        String teamTag
) {}
