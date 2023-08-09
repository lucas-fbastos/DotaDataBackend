package com.br.app.DotaTrainerBackend.mapper;

import com.br.app.DotaTrainerBackend.domain.Enums.GameMode;
import com.br.app.DotaTrainerBackend.domain.Match;
import com.br.app.DotaTrainerBackend.domain.MatchPlayer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class MatchDetailsMapper implements Mapper<Match> {

    @Autowired
    private MatchPlayerMapper matchPlayerMapper;

    @Override
    public Match convert(JSONObject json) {
        Match match = new Match();
        match.setMatchId(json.getLong("match_id"));
        match.setCluster(json.getInt("cluster"));
        long seconds = json.getLong("duration");
        Duration duration = Duration.ofSeconds(seconds);
        match.setDuration(duration);
        Optional<GameMode> optionalGameMode = GameMode.getById(json.getInt("game_mode"));
        optionalGameMode.ifPresent(match::setGameMode);
        match.setDireScore(json.getInt("dire_score"));
        match.setRadiantScore(json.getInt("radiant_score"));
        match.setRadiantWin(json.getBoolean("radiant_win"));
        JSONArray playersJson = json.getJSONArray("players");
        Set<MatchPlayer> radiantPlayers = new HashSet<>();
        Set<MatchPlayer> direPlayers = new HashSet<>();
        List<MatchPlayer> players = matchPlayerMapper.convert(playersJson);
        players.forEach(player -> {
            if(player.getTeam().equals("Radiant"))
                radiantPlayers.add(player);
            else
                direPlayers.add(player);
        });
        match.setRadiantTeam(radiantPlayers);
        match.setDireTeam(direPlayers);
        return match;
    }

    @Override
    public List<Match> convert(JSONArray jsonArray) {
        return null;
    }
}
