package io.luck.DotaTrainerBackend.providers.opendota.service;

import io.luck.DotaTrainerBackend.model.HeroStats;
import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaHeroStats;
import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static java.lang.IO.print;

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
