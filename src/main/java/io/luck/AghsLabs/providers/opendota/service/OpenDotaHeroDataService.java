package io.luck.AghsLabs.providers.opendota.service;

import io.luck.AghsLabs.providers.opendota.model.OpenDotaHero;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class OpenDotaHeroDataService {

    private final RestClient restClient;

    public OpenDotaHeroDataService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Map<Long, OpenDotaHero> getAll() {
        return restClient.get()
                .uri("/constants/heroes")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
