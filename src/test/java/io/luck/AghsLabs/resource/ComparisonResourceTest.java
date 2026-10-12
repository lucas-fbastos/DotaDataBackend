package io.luck.AghsLabs.resource;

import io.luck.AghsLabs.model.dtos.ComparisonDtos.ComparisonOverview;
import io.luck.AghsLabs.model.dtos.ComparisonDtos.DeepComparison;
import io.luck.AghsLabs.service.ComparisonService;
import io.luck.AghsLabs.service.DeepComparisonService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ComparisonResourceTest {

    @Test
    void overviewUsesDocumentedDefaultsAndStableEnvelope() throws Exception {
        var comparisonService = mock(ComparisonService.class);
        var deepService = mock(DeepComparisonService.class);
        when(comparisonService.getOverview(1L, 2L, 30, 10)).thenReturn(
                ComparisonOverview.builder()
                        .leftHeroAggregates(List.of())
                        .rightHeroAggregates(List.of())
                        .pairs(List.of())
                        .warnings(List.of())
                        .build());
        var mvc = MockMvcBuilders.standaloneSetup(
                new ComparisonResource(comparisonService, deepService)).build();

        mvc.perform(get("/compare/players/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.leftHeroAggregates").isArray())
                .andExpect(jsonPath("$.rightHeroAggregates").isArray())
                .andExpect(jsonPath("$.pairs").isArray())
                .andExpect(jsonPath("$.warnings").isArray());
        verify(comparisonService).getOverview(1L, 2L, 30, 10);
    }

    @Test
    void deepRouteBindsBothPlayerMatchAndSlotTriples() throws Exception {
        var comparisonService = mock(ComparisonService.class);
        var deepService = mock(DeepComparisonService.class);
        when(deepService.compare(1L, 10L, 0, 2L, 20L, 128)).thenReturn(
                DeepComparison.builder()
                        .deltaDefinition("LEFT_MINUS_RIGHT")
                        .sameHero(true)
                        .deepComparisonAvailable(true)
                        .farmCheckpoints(List.of())
                        .availabilityWarnings(List.of("UNAVAILABLE"))
                        .build());
        var mvc = MockMvcBuilders.standaloneSetup(
                new ComparisonResource(comparisonService, deepService)).build();

        mvc.perform(get("/compare/matches")
                        .param("leftPlayerId", "1")
                        .param("leftMatchId", "10")
                        .param("leftSlot", "0")
                        .param("rightPlayerId", "2")
                        .param("rightMatchId", "20")
                        .param("rightSlot", "128"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deltaDefinition").value("LEFT_MINUS_RIGHT"))
                .andExpect(jsonPath("$.sameHero").value(true))
                .andExpect(jsonPath("$.farmCheckpoints").isArray())
                .andExpect(jsonPath("$.availabilityWarnings[0]").value("UNAVAILABLE"));
        verify(deepService).compare(1L, 10L, 0, 2L, 20L, 128);
    }

    @Test
    void deepRouteRequiresAllSixReferences() throws Exception {
        var mvc = MockMvcBuilders.standaloneSetup(
                new ComparisonResource(mock(ComparisonService.class), mock(DeepComparisonService.class))).build();

        mvc.perform(get("/compare/matches").param("leftPlayerId", "1"))
                .andExpect(status().isBadRequest());
    }
}
