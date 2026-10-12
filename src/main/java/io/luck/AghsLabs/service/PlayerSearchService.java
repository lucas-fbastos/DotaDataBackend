package io.luck.AghsLabs.service;

import io.luck.AghsLabs.model.Enums.SearchKind;
import io.luck.AghsLabs.model.dtos.PlayerSearchResult;
import io.luck.AghsLabs.model.entity.ProPlayer;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayerSearchResult;
import io.luck.AghsLabs.providers.opendota.service.OpenDotaPlayerDataService;
import io.luck.AghsLabs.repository.ProPlayerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlayerSearchService {
    private static final int RESULT_LIMIT = 20;

    private final OpenDotaPlayerDataService playerDataService;
    private final ProPlayerRepository proPlayerRepository;

    public PlayerSearchService(OpenDotaPlayerDataService playerDataService,
                               ProPlayerRepository proPlayerRepository) {
        this.playerDataService = playerDataService;
        this.proPlayerRepository = proPlayerRepository;
    }

    public List<PlayerSearchResult> search(String query, SearchKind searchKind) {
        return searchKind == SearchKind.PRO ? searchPros(query) : searchAny(query);
    }

    private List<PlayerSearchResult> searchAny(String query) {
        PlayerSearchResult numericResult = numericResult(query, false);
        if (numericResult != null) return java.util.List.of(numericResult);

        var providerResults = playerDataService.searchPlayers(query);
        if (providerResults == null || providerResults.isEmpty()) return java.util.List.of();

        var ids = providerResults.stream()
                .map(OpenDotaPlayerSearchResult::accountId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, ProPlayer> prosById = proPlayerRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(ProPlayer::getAccountId, Function.identity()));

        return providerResults.stream()
                .filter(result -> result.accountId() != null)
                .limit(RESULT_LIMIT)
                .map(result -> {
                    ProPlayer pro = prosById.get(result.accountId());
                    return PlayerSearchResult.builder()
                            .accountId(result.accountId())
                            .personaName(result.personaName())
                            .avatarFull(result.avatarFull())
                            .lastMatchTime(result.lastMatchTime())
                            .similarity(result.similarity())
                            .pro(pro != null)
                            .teamName(pro == null ? null : pro.getTeamName())
                            .teamTag(pro == null ? null : pro.getTeamTag())
                            .build();
                })
                .toList();
    }

    private List<PlayerSearchResult> searchPros(String query) {
        PlayerSearchResult numericResult = numericResult(query, true);
        if (numericResult != null) return java.util.List.of(numericResult);

        String normalized = query.toLowerCase(Locale.ROOT);
        return proPlayerRepository.search(normalized, PageRequest.of(0, RESULT_LIMIT)).stream()
                .map(pro -> PlayerSearchResult.builder()
                        .accountId(pro.getAccountId())
                        .personaName(pro.getPersonaName())
                        .avatarFull(pro.getAvatarImageFull())
                        .lastMatchTime(pro.getLastLogin())
                        .pro(true)
                        .teamName(pro.getTeamName())
                        .teamTag(pro.getTeamTag())
                        .build())
                .toList();
    }

    private PlayerSearchResult numericResult(String query, boolean proOnly) {
        if (!query.chars().allMatch(Character::isDigit)) return null;

        final long accountId;
        try {
            accountId = Long.parseLong(query);
        } catch (NumberFormatException exception) {
            return null;
        }
        if (accountId <= 0) return null;

        ProPlayer pro = proPlayerRepository.findById(accountId).orElse(null);
        if (proOnly && pro == null) return null;
        return PlayerSearchResult.builder()
                .accountId(accountId)
                .personaName(pro == null ? "Account " + accountId : pro.getPersonaName())
                .avatarFull(pro == null ? null : pro.getAvatarImageFull())
                .lastMatchTime(pro == null ? null : pro.getLastLogin())
                .pro(pro != null)
                .teamName(pro == null ? null : pro.getTeamName())
                .teamTag(pro == null ? null : pro.getTeamTag())
                .build();
    }

}
