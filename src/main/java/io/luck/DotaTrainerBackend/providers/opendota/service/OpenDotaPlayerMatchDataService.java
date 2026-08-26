package io.luck.DotaTrainerBackend.providers.opendota.service;

import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaPlayerMatch;
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
}
