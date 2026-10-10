package io.luck.DotaTrainerBackend.mapper;

import io.luck.DotaTrainerBackend.model.Enums.Faction;
import io.luck.DotaTrainerBackend.model.Enums.GameMode;
import io.luck.DotaTrainerBackend.model.Match;
import io.luck.DotaTrainerBackend.model.MatchPlayer;
import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaPlayerMatchDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
public class MatchDetailsMapper implements Mapper<Match, OpenDotaPlayerMatchDetails> {

    @Autowired
    private MatchPlayerMapper matchPlayerMapper;

    @Override
    public Match convert(OpenDotaPlayerMatchDetails openDotaMatchDetails) {
        Match match = new Match();
        match.setMatchId(openDotaMatchDetails.matchId());
        match.setCluster(openDotaMatchDetails.cluster());
        long seconds = openDotaMatchDetails.duration();
        Duration duration = Duration.ofSeconds(seconds);
        match.setDuration(duration);
        Optional<GameMode> optionalGameMode = GameMode.getById(openDotaMatchDetails.gameMode());
        optionalGameMode.ifPresent(match::setGameMode);
        match.setDireScore(openDotaMatchDetails.direScore());
        match.setRadiantScore(openDotaMatchDetails.radiantScore());
        match.setRadiantWin(openDotaMatchDetails.radianWin());
        Set<MatchPlayer> radiantPlayers = new HashSet<>();
        Set<MatchPlayer> direPlayers = new HashSet<>();
        Set<MatchPlayer> players = matchPlayerMapper.convert(openDotaMatchDetails.players());
        players.forEach(player -> {
            if(player.team().equals(Faction.RADIANT.name()))
                radiantPlayers.add(player);
            else
                direPlayers.add(player);
        });
        match.setRadiantTeam(radiantPlayers);
        match.setDireTeam(direPlayers);
        return match;
    }

    @Override
    public Collection<Match> convert(Collection<OpenDotaPlayerMatchDetails> items) {
        return List.of();
    }

}
