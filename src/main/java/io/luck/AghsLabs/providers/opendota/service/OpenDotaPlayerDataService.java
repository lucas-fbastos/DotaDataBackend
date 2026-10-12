package io.luck.AghsLabs.providers.opendota.service;

import io.luck.AghsLabs.config.CacheConfig;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaHeroStats;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayer;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayerSearchResult;
import io.luck.AghsLabs.exception.ExternalApiException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
public class OpenDotaPlayerDataService {

    private final RestClient restClient;

    public OpenDotaPlayerDataService(RestClient restClient) {
        this.restClient = restClient;
    }

    public OpenDotaPlayer getById(
            @NonNull Long playerId
    ) {
        return restClient.get()
                .uri("/players/{playerId}", playerId)
                .retrieve()
                .body(OpenDotaPlayer.class);
    }

    @Cacheable(cacheNames = CacheConfig.PLAYER_SEARCH, key = "#query.toLowerCase()",
            unless = "#result == null || #result.isEmpty()")
    public List<OpenDotaPlayerSearchResult> searchPlayers(@NonNull String query) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("search").queryParam("q", query).build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException exception) {
            throw new ExternalApiException("OpenDota player search is currently unavailable", exception);
        }
    }


    public List<OpenDotaHeroStats> getPlayerHeroes(
            @NonNull Long playerId,
            @NonNull Integer patchId,
            @Nullable Integer pageSize,
            @Nullable Integer page
    ) {
        var heroStats = restClient.get()
                .uri("/players/{playerId}/heroes?significant=0&days=60&patch={patchId}",
                        playerId, patchId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<OpenDotaHeroStats>>() {});

        assert heroStats != null;

        if(page != null && pageSize != null )
            return heroStats.stream().skip(page).limit(pageSize).toList();

        return heroStats;
    }

}
