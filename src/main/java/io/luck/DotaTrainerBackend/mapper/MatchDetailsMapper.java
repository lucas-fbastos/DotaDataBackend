package io.luck.DotaTrainerBackend.mapper;

import io.luck.DotaTrainerBackend.model.Enums.Faction;
import io.luck.DotaTrainerBackend.model.Enums.GameMode;
import io.luck.DotaTrainerBackend.model.Match;
import io.luck.DotaTrainerBackend.model.MatchPlayer;
import io.luck.DotaTrainerBackend.model.entity.Hero;
import io.luck.DotaTrainerBackend.model.entity.Item;
import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaPlayerMatchDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
public class MatchDetailsMapper {

    private final MatchPlayerMapper matchPlayerMapper;

    public MatchDetailsMapper(MatchPlayerMapper matchPlayerMapper) {
        this.matchPlayerMapper = matchPlayerMapper;
    }

    public Match convert(OpenDotaPlayerMatchDetails openDotaMatchDetails,
                         Map<Long, Hero> heroesById,
                         Map<Long, Item> itemsById) {
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
        Set<MatchPlayer> players = matchPlayerMapper.convert(openDotaMatchDetails.players(), heroesById, itemsById);
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

}
