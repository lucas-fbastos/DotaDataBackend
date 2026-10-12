package io.luck.AghsLabs.service;

import io.luck.AghsLabs.model.dtos.ComparisonDtos.CompatibilityReason;
import io.luck.AghsLabs.model.dtos.ComparisonDtos.CompatibilityTier;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayerMatchCandidate;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatchPairingServiceTest {
    private final MatchPairingService service = new MatchPairingService();

    @Test
    void returnsAtMostTenUniqueCompletePairs() {
        var left = IntStream.range(0, 15)
                .mapToObj(index -> candidate(1_000L + index, (long) (index % 4 + 1), 0,
                        1_800_000_000L - index * 1_000L))
                .toList();
        var right = IntStream.range(0, 15)
                .mapToObj(index -> candidate(2_000L + index, (long) (index % 4 + 1), 128,
                        1_800_000_000L - index * 1_100L))
                .toList();

        var pairs = service.pair(left, right, 10);

        assertEquals(10, pairs.size());
        assertEquals(10, pairs.stream().map(pair -> pair.left().matchId()).distinct().count());
        assertEquals(10, pairs.stream().map(pair -> pair.right().matchId()).distinct().count());
        assertTrue(pairs.stream().allMatch(pair -> pair.left() != null && pair.right() != null));
    }

    @Test
    void sameHeroOutranksOtherwisePerfectDifferentHeroPair() {
        var left = candidate(100L, 7L, 0, 1_800_000_000L);
        var sameHeroWithDifferentContext = OpenDotaPlayerMatchCandidate.builder()
                .matchId(200L).heroId(7L).playerSlot(128).startTime(1_700_000_000L)
                .duration(3_000).patch(59).laneRole(3).gameMode(3).lobbyType(7).leaverStatus(0)
                .build();
        var differentHeroPerfectContext = candidate(201L, 8L, 128, 1_800_000_000L);

        var result = service.pair(List.of(left),
                List.of(differentHeroPerfectContext, sameHeroWithDifferentContext), 1);

        assertEquals(200L, result.getFirst().right().matchId());
        assertTrue(result.getFirst().deepComparisonAvailable());
        assertTrue(result.getFirst().compatibilityReasons().contains(CompatibilityReason.SAME_HERO));
    }

    @Test
    void assignsCompatibilityTiersAndRejectsShortOrAbandonedMatches() {
        var left = candidate(100L, 7L, 0, 1_800_000_000L);
        var right = candidate(200L, 7L, 128, 1_800_000_100L);
        var shortMatch = OpenDotaPlayerMatchCandidate.builder()
                .matchId(101L).heroId(7L).playerSlot(0).startTime(1_800_000_000L)
                .duration(599).leaverStatus(0).build();
        var abandoned = OpenDotaPlayerMatchCandidate.builder()
                .matchId(102L).heroId(7L).playerSlot(0).startTime(1_800_000_000L)
                .duration(1_800).leaverStatus(2).build();

        var result = service.pair(List.of(shortMatch, abandoned, left), List.of(right), 10);

        assertEquals(1, result.size());
        assertEquals(CompatibilityTier.OPTIMAL, result.getFirst().compatibilityTier());
        assertFalse(service.isEligible(shortMatch));
        assertFalse(service.isEligible(abandoned));
    }

    private OpenDotaPlayerMatchCandidate candidate(Long matchId, Long heroId,
                                                    Integer playerSlot, Long startTime) {
        return OpenDotaPlayerMatchCandidate.builder()
                .matchId(matchId)
                .heroId(heroId)
                .playerSlot(playerSlot)
                .radiantWin(true)
                .startTime(startTime)
                .duration(1_800)
                .patch(60)
                .laneRole(1)
                .gameMode(2)
                .lobbyType(1)
                .leaverStatus(0)
                .build();
    }
}
