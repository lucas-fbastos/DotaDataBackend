package io.luck.AghsLabs.service;

import io.luck.AghsLabs.model.Enums.SearchKind;
import io.luck.AghsLabs.model.entity.ProPlayer;
import io.luck.AghsLabs.providers.opendota.service.OpenDotaPlayerDataService;
import io.luck.AghsLabs.repository.ProPlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class PlayerSearchServiceTest {

    @Test
    void textProSearchDelegatesFilteringSortingAndLimitToRepository() {
        var playerDataService = mock(OpenDotaPlayerDataService.class);
        var proPlayerRepository = mock(ProPlayerRepository.class);
        var pro = new ProPlayer();
        pro.setAccountId(101L);
        pro.setPersonaName("Miracle-");
        pro.setAvatarImageFull("avatar");
        pro.setLastLogin("2026-01-01");
        pro.setTeamName("Nigma Galaxy");
        pro.setTeamTag("NGX");
        var firstTwenty = PageRequest.of(0, 20);
        when(proPlayerRepository.search("miracle", firstTwenty)).thenReturn(List.of(pro));
        var service = new PlayerSearchService(playerDataService, proPlayerRepository);

        var results = service.search("MiRaClE", SearchKind.PRO);

        assertEquals(1, results.size());
        assertEquals(101L, results.getFirst().accountId());
        assertEquals("Miracle-", results.getFirst().personaName());
        assertEquals("NGX", results.getFirst().teamTag());
        assertTrue(results.getFirst().pro());
        verify(proPlayerRepository).search("miracle", firstTwenty);
        verifyNoMoreInteractions(proPlayerRepository);
        verifyNoInteractions(playerDataService);
    }

    @Test
    void numericAnySearchReturnsSelectableAccountWithoutCallingOpenDotaSearch() {
        var playerDataService = mock(OpenDotaPlayerDataService.class);
        var proPlayerRepository = mock(ProPlayerRepository.class);
        when(proPlayerRepository.findById(123456L)).thenReturn(Optional.empty());
        var service = new PlayerSearchService(playerDataService, proPlayerRepository);

        var results = service.search("123456", SearchKind.ANY);

        assertEquals(1, results.size());
        assertEquals(123456L, results.getFirst().accountId());
        assertEquals("Account 123456", results.getFirst().personaName());
        assertFalse(results.getFirst().pro());
        verifyNoInteractions(playerDataService);
    }

    @Test
    void numericProSearchOnlyReturnsKnownProAccounts() {
        var playerDataService = mock(OpenDotaPlayerDataService.class);
        var proPlayerRepository = mock(ProPlayerRepository.class);
        var pro = new ProPlayer();
        pro.setAccountId(7654321L);
        pro.setPersonaName("Known Pro");
        pro.setTeamName("Team");
        pro.setTeamTag("TAG");
        when(proPlayerRepository.findById(7654321L)).thenReturn(Optional.of(pro));
        var service = new PlayerSearchService(playerDataService, proPlayerRepository);

        var results = service.search("7654321", SearchKind.PRO);

        assertEquals(1, results.size());
        assertTrue(results.getFirst().pro());
        assertEquals("Known Pro", results.getFirst().personaName());
        verifyNoInteractions(playerDataService);
    }
}
