package io.luck.AghsLabs.repository;

import io.luck.AghsLabs.model.entity.ProPlayer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProPlayerRepositoryTest {

    @Autowired
    private ProPlayerRepository proPlayerRepository;

    @Test
    void searchMatchesAllSupportedFieldsIgnoringStoredValueCase() {
        proPlayerRepository.saveAllAndFlush(List.of(
                player(1L, "NEEDLE Persona", null, null, null),
                player(2L, null, "The NeEdLe Name", null, null),
                player(3L, null, null, "NEEDLE United", null),
                player(4L, null, null, null, "nEeDlE"),
                player(5L, null, null, null, null)
        ));

        var results = proPlayerRepository.search("needle", PageRequest.of(0, 10));

        Set<Long> accountIds = results.stream()
                .map(ProPlayer::getAccountId)
                .collect(Collectors.toSet());
        assertEquals(Set.of(1L, 2L, 3L, 4L), accountIds);
    }

    @Test
    void searchOrdersExactPersonaThenPrefixThenGeneralMatches() {
        proPlayerRepository.saveAllAndFlush(List.of(
                player(90L, "mIrAcLe", "unrelated", "unrelated", "unrelated"),
                player(30L, "Miracle Alpha", "unrelated", "unrelated", "unrelated"),
                player(20L, "MIRACLE ALPHA", "unrelated", "unrelated", "unrelated"),
                player(50L, "Aardvark", "Coach Miracle", "unrelated", "unrelated"),
                player(60L, "The Miracle", "unrelated", "unrelated", "unrelated")
        ));

        var results = proPlayerRepository.search("miracle", PageRequest.of(0, 10));

        assertEquals(
                List.of(90L, 20L, 30L, 50L, 60L),
                results.stream().map(ProPlayer::getAccountId).toList()
        );
    }

    @Test
    void searchAppliesThePageableLimitAfterRanking() {
        proPlayerRepository.saveAllAndFlush(List.of(
                player(1L, "Target", "unrelated", "unrelated", "unrelated"),
                player(2L, "Target Alpha", "unrelated", "unrelated", "unrelated"),
                player(3L, "Target Beta", "unrelated", "unrelated", "unrelated"),
                player(4L, "The Target", "unrelated", "unrelated", "unrelated")
        ));

        var results = proPlayerRepository.search("target", PageRequest.of(0, 2));

        assertEquals(2, results.size());
        assertEquals(List.of(1L, 2L), results.stream().map(ProPlayer::getAccountId).toList());
    }

    private ProPlayer player(
            Long accountId,
            String personaName,
            String name,
            String teamName,
            String teamTag
    ) {
        var player = new ProPlayer();
        player.setAccountId(accountId);
        player.setPersonaName(personaName);
        player.setName(name);
        player.setTeamName(teamName);
        player.setTeamTag(teamTag);
        return player;
    }
}
