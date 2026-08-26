package io.luck.DotaTrainerBackend.model;

import io.luck.DotaTrainerBackend.model.Enums.Faction;
import io.luck.DotaTrainerBackend.model.Enums.GameMode;
import io.luck.DotaTrainerBackend.model.Enums.LeaverStatus;
import io.luck.DotaTrainerBackend.model.entity.Hero;
import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaPlayerMatch;

import java.time.Duration;
import java.time.Instant;

public record MatchSummary(
        Long matchId,
        Faction faction,
        boolean playerWon,
        Duration duration,
        GameMode gameMode,
        Integer lobbyType,
        Hero hero,
        Instant matchStart,
        Integer version,
        Integer kills,
        Integer deaths,
        Integer assists,
        Integer skill,
        Integer averageRank,
        Integer xpPerMinute,
        Integer goldPerMinute,
        Integer heroDamage,
        Integer heroHealing,
        Integer lastHits,
        Integer lane,
        Integer laneRole,
        Boolean roaming,
        Integer cluster,
        LeaverStatus leaverStatus,
        Integer partySize
) {

    public static MatchSummary from(OpenDotaPlayerMatch match, Hero hero) {
        Faction faction = Faction.fromPlayerSlot(match.playerSlot());

        return new MatchSummary(
                match.matchId(),
                faction,
                faction.won(match.radiantWin()),
                Duration.ofSeconds(match.duration()),
                GameMode.getById(match.gameMode()).orElse(GameMode.UNKNOWN),
                match.lobbyType(),
                hero,
                Instant.ofEpochSecond(match.startTime()),
                match.version(),
                match.kills(),
                match.deaths(),
                match.assists(),
                match.skill(),
                match.averageRank(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                LeaverStatus.fromValue(match.leaverStatus()),
                match.partySize()
        );
    }
}