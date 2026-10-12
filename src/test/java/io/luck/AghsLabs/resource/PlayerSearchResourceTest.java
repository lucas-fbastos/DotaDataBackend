package io.luck.AghsLabs.resource;

import io.luck.AghsLabs.model.Enums.SearchKind;
import io.luck.AghsLabs.model.dtos.PlayerSearchResult;
import io.luck.AghsLabs.service.PlayerSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PlayerSearchResourceTest {

    @Test
    void pluralSearchRouteDefaultsToAnyAndReturnsSmallContract() throws Exception {
        var service = mock(PlayerSearchService.class);
        when(service.search("miracle", SearchKind.ANY)).thenReturn(List.of(
                PlayerSearchResult.builder()
                        .accountId(1L)
                        .personaName("Miracle-")
                        .avatarFull("avatar")
                        .lastMatchTime("2026-01-01")
                        .similarity(0.9)
                        .pro(true)
                        .teamName("Team")
                        .teamTag("TAG")
                        .build()));
        var mvc = MockMvcBuilders.standaloneSetup(new PlayerSearchResource(service)).build();

        mvc.perform(get("/players/search").param("q", "miracle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value(1))
                .andExpect(jsonPath("$[0].personaName").value("Miracle-"))
                .andExpect(jsonPath("$[0].pro").value(true))
                .andExpect(jsonPath("$[0].teamTag").value("TAG"));
        verify(service).search("miracle", SearchKind.ANY);
    }

    @Test
    void singularAliasAndProKindRemainSupported() throws Exception {
        var service = mock(PlayerSearchService.class);
        when(service.search("nisha", SearchKind.PRO)).thenReturn(List.of());
        var mvc = MockMvcBuilders.standaloneSetup(new PlayerSearchResource(service)).build();

        mvc.perform(get("/player/search").param("q", "nisha").param("kind", "pro"))
                .andExpect(status().isOk());
        verify(service).search("nisha", SearchKind.PRO);
    }
}
