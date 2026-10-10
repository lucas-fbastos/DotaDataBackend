package io.luck.DotaTrainerBackend.providers.opendota.service;

import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaPatch;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OpenDotaPatchDataService {

    private final RestClient restClient;

    public OpenDotaPatchDataService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<OpenDotaPatch> getAll() {
        return restClient.get()
                .uri("/constants/patch")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
