package io.luck.AghsLabs.mapper;

import io.luck.AghsLabs.model.Enums.Faction;
import io.luck.AghsLabs.model.Enums.GameMode;
import io.luck.AghsLabs.model.Match;
import io.luck.AghsLabs.model.MatchChat;
import io.luck.AghsLabs.model.MatchWards;
import io.luck.AghsLabs.model.MatchPlayer;
import io.luck.AghsLabs.model.entity.Hero;
import io.luck.AghsLabs.model.entity.Item;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayerMatchDetails;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.*;

@Component
public class MatchDetailsMapper {
    private final MatchPlayerMapper matchPlayerMapper;

    public MatchDetailsMapper(MatchPlayerMapper matchPlayerMapper) {
        this.matchPlayerMapper = matchPlayerMapper;
    }

    public Match convert(OpenDotaPlayerMatchDetails source, Map<Long, Hero> heroesById, Map<Long, Item> itemsById) {
        Match match = new Match();
        match.setVersion(source.version());
        match.setMatchId(source.matchId());
        match.setTeamfights(MatchDetailsDataMapper.mapList(source.teamfights(), MatchDetailsDataMapper::convert));
        match.setPauses(MatchDetailsDataMapper.mapList(source.pauses(), MatchDetailsDataMapper::convert));
        match.setObjectives(MatchDetailsDataMapper.mapList(source.objectives(), MatchDetailsDataMapper::convert));
        match.setRadiantGoldAdv(source.radiantGoldAdv());
        match.setRadiantXpAdv(source.radiantXpAdv());
        match.setDraftTimings(MatchDetailsDataMapper.mapList(source.draftTimings(), MatchDetailsDataMapper::convert));
        match.setLeagueId(source.leagueId());
        match.setStartTime(source.startTime());
        match.setDuration(source.duration() == null ? null : Duration.ofSeconds(source.duration()));
        match.setSeriesId(source.seriesId());
        match.setSeriesType(source.seriesType());
        match.setCluster(source.cluster());
        match.setReplaySalt(source.replaySalt());
        match.setRadiantWin(source.radiantWin());
        match.setPreGameDuration(source.preGameDuration());
        match.setMatchSeqNum(source.matchSeqNum());
        match.setTowerStatusRadiant(source.towerStatusRadiant());
        match.setTowerStatusDire(source.towerStatusDire());
        match.setBarracksStatusRadiant(source.barracksStatusRadiant());
        match.setBarracksStatusDire(source.barracksStatusDire());
        match.setFirstBloodTime(source.firstBloodTime());
        match.setLobbyType(source.lobbyType());
        match.setHumanPlayers(source.humanPlayers());
        match.setGameMode(source.gameMode() == null ? null : GameMode.getById(source.gameMode()).orElse(null));
        match.setFlags(source.flags());
        match.setEngine(source.engine());
        match.setRadiantScore(source.radiantScore());
        match.setDireScore(source.direScore());
        match.setRadiantTeamId(source.radiantTeamId());
        match.setRadiantName(source.radiantName());
        match.setRadiantLogo(source.radiantLogo());
        match.setRadiantTeamComplete(source.radiantTeamComplete());
        match.setDireTeamId(source.direTeamId());
        match.setDireName(source.direName());
        match.setDireLogo(source.direLogo());
        match.setDireTeamComplete(source.direTeamComplete());
        match.setRadiantCaptain(source.radiantCaptain());
        match.setDireCaptain(source.direCaptain());
        match.setPicksBans(MatchDetailsDataMapper.mapList(source.picksBans(), MatchDetailsDataMapper::convert));
        match.setOdData(MatchDetailsDataMapper.convert(source.odData()));
        match.setLeague(MatchDetailsDataMapper.convert(source.league()));
        match.setRadiantTeamInfo(MatchDetailsDataMapper.convert(source.radiantTeam()));
        match.setDireTeamInfo(MatchDetailsDataMapper.convert(source.direTeam()));
        match.setMetadata(source.metadata());
        match.setReplayUrl(source.replayUrl());
        match.setPatch(source.patch());
        match.setRegion(source.region());
        match.setThrowAmount(source.throwAmount());
        match.setLoss(source.loss());
        Set<MatchPlayer> players = matchPlayerMapper.convert(source.players(), heroesById, itemsById);
        match.setPlayers(new ArrayList<>(players));
        Set<MatchPlayer> radiant = new LinkedHashSet<>();
        Set<MatchPlayer> dire = new LinkedHashSet<>();
        for (var player : players) {
            if (Faction.RADIANT.name().equals(player.team())) radiant.add(player);
            else dire.add(player);
        }
        match.setRadiantTeam(radiant);
        match.setDireTeam(dire);
        return match;
    }

    public MatchChat convertChat(OpenDotaPlayerMatchDetails source) {
        Map<Integer, Long> accountIdsByPlayerSlot = new HashMap<>();
        if (source.players() != null) {
            for (var player : source.players()) {
                if (player.playerSlot() != null) {
                    accountIdsByPlayerSlot.put(player.playerSlot(), player.accountId());
                }
            }
        }
        return new MatchChat(
                MatchDetailsDataMapper.mapList(source.chat(), entry -> MatchDetailsDataMapper.convert(
                        entry, entry == null ? null : accountIdsByPlayerSlot.get(entry.playerSlot()))),
                source.allWordCounts(),
                source.myWordCounts()
        );
    }

    public MatchWards convertWards(OpenDotaPlayerMatchDetails source) {
        if (source.players() == null) return new MatchWards(List.of());
        return new MatchWards(MatchDetailsDataMapper.mapList(source.players(), player ->
                new MatchWards.PlayerWards(
                        player.playerSlot(),
                        player.accountId(),
                        player.heroId(),
                        MatchDetailsDataMapper.mapList(player.obsLog(), MatchDetailsDataMapper::convert),
                        MatchDetailsDataMapper.mapList(player.senLog(), MatchDetailsDataMapper::convert),
                        MatchDetailsDataMapper.mapList(player.obsLeftLog(), MatchDetailsDataMapper::convert),
                        MatchDetailsDataMapper.mapList(player.senLeftLog(), MatchDetailsDataMapper::convert)
                )));
    }
}
