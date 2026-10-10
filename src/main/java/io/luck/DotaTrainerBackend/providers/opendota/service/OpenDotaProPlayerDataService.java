package io.luck.DotaTrainerBackend.providers.opendota.service;

import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaProPlayer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OpenDotaProPlayerDataService {

    private final RestClient restClient;

    public OpenDotaProPlayerDataService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<OpenDotaProPlayer> getProPlayers() {
        return restClient.get()
                .uri("/proPlayers")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
