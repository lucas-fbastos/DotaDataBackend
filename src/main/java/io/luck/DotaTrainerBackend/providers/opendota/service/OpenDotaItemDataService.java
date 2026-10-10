package io.luck.DotaTrainerBackend.providers.opendota.service;

import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaItem;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class OpenDotaItemDataService {

    private final RestClient restClient;

    public OpenDotaItemDataService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Map<String, OpenDotaItem> getAll() {
        return restClient.get()
                .uri("/constants/items")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
