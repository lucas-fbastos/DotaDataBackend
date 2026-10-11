package io.luck.AghsLabs.providers.opendota.service;

import io.luck.AghsLabs.providers.opendota.model.OpenDotaLiveMatch;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OpenDotaLiveDataService {

    private final RestClient restClient;

    public OpenDotaLiveDataService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<OpenDotaLiveMatch> getLiveMatches() {
        return restClient.get()
                .uri("/live")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
