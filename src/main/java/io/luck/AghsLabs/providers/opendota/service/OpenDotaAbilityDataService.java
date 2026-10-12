package io.luck.AghsLabs.providers.opendota.service;

import io.luck.AghsLabs.providers.opendota.model.OpenDotaAbility;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaAbilityId;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Arrays;

@Service
public class OpenDotaAbilityDataService {

    private final RestClient restClient;

    public OpenDotaAbilityDataService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<OpenDotaAbilityId> getIds() {
        Map<String, String> abilityIds = restClient.get()
                .uri("/constants/ability_ids")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (abilityIds == null) {
            return List.of();
        }

        return abilityIds.entrySet().stream()
                .flatMap(entry -> Arrays.stream(entry.getKey().split(","))
                        .map(String::trim)
                        .filter(id -> !id.isEmpty())
                        .map(id -> new OpenDotaAbilityId(Long.parseLong(id), entry.getValue())))
                .toList();
    }

    public Map<String, OpenDotaAbility> getAll() {
        return restClient.get()
                .uri("/constants/abilities")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
