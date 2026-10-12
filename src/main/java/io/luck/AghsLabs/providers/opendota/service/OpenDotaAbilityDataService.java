package io.luck.AghsLabs.providers.opendota.service;

import io.luck.AghsLabs.providers.opendota.model.OpenDotaAbility;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaAbilityId;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class OpenDotaAbilityDataService {

    private final RestClient restClient;

    public OpenDotaAbilityDataService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<OpenDotaAbilityId> getIds() {
        Map<Long, String> abilityIds = restClient.get()
                .uri("/constants/ability_ids")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (abilityIds == null) {
            return List.of();
        }

        return abilityIds.entrySet().stream()
                .map(entry -> new OpenDotaAbilityId(entry.getKey(), entry.getValue()))
                .toList();
    }

    public Map<String, OpenDotaAbility> getAll() {
        return restClient.get()
                .uri("/constants/abilities")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
