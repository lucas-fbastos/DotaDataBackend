package io.luck.DotaTrainerBackend.providers.opendota.service;

import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaPlayerMatch;
import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaPlayerMatchDetails;
import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaPlayerRatio;
import org.jspecify.annotations.NonNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OpenDotaPlayerMatchDataService {

    private final RestClient restClient;

    public OpenDotaPlayerMatchDataService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<OpenDotaPlayerMatch> getPlayerRecentMatches(Long playerId){
        return restClient.get()
                .uri("players/{playerId}/recentMatches",playerId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public OpenDotaPlayerRatio getPlayerRatio(@NonNull Long playerId){
        return restClient.get()
                .uri("players/{playerId}/wl",playerId)
                .retrieve()
                .body(OpenDotaPlayerRatio.class);
    }

    public OpenDotaPlayerMatchDetails getMatchDetails(@NonNull Long matchId){
        return restClient.get()
                .uri("matches/"+matchId)
                .retrieve()
                .body(OpenDotaPlayerMatchDetails.class);
    }
}
