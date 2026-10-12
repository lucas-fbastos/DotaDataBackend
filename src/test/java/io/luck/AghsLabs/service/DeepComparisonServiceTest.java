package io.luck.AghsLabs.service;

import io.luck.AghsLabs.exception.BadRequestException;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaMatchDetailsData;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaMatchPlayer;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayerMatchDetails;
import io.luck.AghsLabs.providers.opendota.service.OpenDotaPlayerMatchDataService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeepComparisonServiceTest {

    @Test
    void unparsedMatchesReturnFocusedPartialResponseWithWarnings() {
        var provider = mock(OpenDotaPlayerMatchDataService.class);
        var service = new DeepComparisonService(provider, mock(ItemService.class));
        when(provider.getMatchDetails(10L)).thenReturn(details(10L,
                OpenDotaMatchPlayer.builder().accountId(1L).playerSlot(0).heroId(7L).build()));
        when(provider.getMatchDetails(20L)).thenReturn(details(20L,
                OpenDotaMatchPlayer.builder().accountId(2L).playerSlot(128).heroId(7L).build()));

        var result = service.compare(1L, 10L, 0, 2L, 20L, 128);

        assertTrue(result.sameHero());
        assertTrue(result.deepComparisonAvailable());
        assertFalse(result.completeData());
        assertFalse(result.cacheable());
        assertTrue(result.availabilityWarnings().contains("LEFT_MATCH_NOT_PARSED"));
        assertTrue(result.availabilityWarnings().contains("RIGHT_FARM_TIMELINE_UNAVAILABLE"));
        assertTrue(result.farmCheckpoints().isEmpty());
        assertTrue(result.abilityComparison().leftOrderIds().isEmpty());
    }

    @Test
    void parsedMatchesProduceLeftMinusRightTimelinesAndAggregates() {
        var provider = mock(OpenDotaPlayerMatchDataService.class);
        var service = new DeepComparisonService(provider, mock(ItemService.class));
        var leftPlayer = parsedPlayer(1L, 0, 7L, 100L, 10L, 5);
        var rightPlayer = parsedPlayer(2L, 128, 7L, 80L, 8L, 3);
        when(provider.getMatchDetails(10L)).thenReturn(parsedDetails(10L, leftPlayer, 1_000L));
        when(provider.getMatchDetails(20L)).thenReturn(parsedDetails(20L, rightPlayer, 2_000L));

        var result = service.compare(1L, 10L, 0, 2L, 20L, 128);

        assertTrue(result.completeData());
        assertEquals("LEFT_MINUS_RIGHT", result.deltaDefinition());
        assertEquals(2, result.summaryDelta().kills());
        assertEquals(20L, result.farmCheckpoints().getLast().delta().gold());
        assertEquals(20, result.itemComparison().purchaseTimings().getFirst().deltaSeconds());
        assertEquals(List.of(101, 102), result.abilityComparison().leftOrderIds());
        assertEquals(1_000L, result.teamfightComparison().left().damage());
        assertEquals(-1_000L, result.teamfightComparison().delta().damage());
    }

    @Test
    void participantMustMatchBothAccountAndSlot() {
        var provider = mock(OpenDotaPlayerMatchDataService.class);
        var service = new DeepComparisonService(provider, mock(ItemService.class));
        when(provider.getMatchDetails(10L)).thenReturn(details(10L,
                OpenDotaMatchPlayer.builder().accountId(1L).playerSlot(0).heroId(7L).build()));
        when(provider.getMatchDetails(20L)).thenReturn(details(20L,
                OpenDotaMatchPlayer.builder().accountId(2L).playerSlot(128).heroId(7L).build()));

        assertThrows(BadRequestException.class,
                () -> service.compare(1L, 10L, 1, 2L, 20L, 128));
    }

    private OpenDotaPlayerMatchDetails details(Long matchId, OpenDotaMatchPlayer player) {
        return OpenDotaPlayerMatchDetails.builder().matchId(matchId).players(List.of(player)).build();
    }

    private OpenDotaMatchPlayer parsedPlayer(Long accountId, Integer slot, Long heroId,
                                             Long finalGold, Long finalLastHits, Integer kills) {
        return OpenDotaMatchPlayer.builder()
                .accountId(accountId).playerSlot(slot).heroId(heroId).radiantWin(slot < 128)
                .times(List.of(0, 60)).goldT(List.of(0L, finalGold)).lhT(List.of(0L, finalLastHits))
                .dnT(List.of(0L, 2L)).xpT(List.of(0L, finalGold * 2)).netWorthT(List.of(600L, finalGold))
                .purchaseLog(List.of(new OpenDotaMatchDetailsData.PurchaseLog(
                        accountId == 1L ? 100 : 80, "boots", null)))
                .abilityUpgradesArr(List.of(101, 102))
                .kills(kills).deaths(2).assists(8).lastHits(finalLastHits.intValue()).denies(2)
                .goldPerMin(500).xpPerMin(600).netWorth(finalGold).level(20)
                .heroDamage(10_000L).towerDamage(2_000L).heroHealing(100L)
                .teamfightParticipation(0.75)
                .build();
    }

    private OpenDotaPlayerMatchDetails parsedDetails(Long matchId, OpenDotaMatchPlayer player,
                                                     Long teamfightDamage) {
        var teamfightPlayer = new OpenDotaMatchDetailsData.TeamfightPlayer(
                null, null, null, null, null, 1, 0, teamfightDamage, 100L,
                200, 300, 1_000, 1_300);
        return OpenDotaPlayerMatchDetails.builder()
                .matchId(matchId).version(1).duration(1_800L)
                .odData(new OpenDotaMatchDetailsData.OdData(true, true, true, true))
                .players(List.of(player))
                .teamfights(List.of(new OpenDotaMatchDetailsData.Teamfight(
                        600, 630, 625, 2, List.of(teamfightPlayer))))
                .build();
    }
}
