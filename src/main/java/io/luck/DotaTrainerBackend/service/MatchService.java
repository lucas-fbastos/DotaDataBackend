package io.luck.DotaTrainerBackend.service;

import io.luck.DotaTrainerBackend.model.entity.Hero;
import io.luck.DotaTrainerBackend.model.Match;
import io.luck.DotaTrainerBackend.model.MatchSummary;
import io.luck.DotaTrainerBackend.mapper.MatchDetailsMapper;
import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaPlayerMatch;
import org.json.JSONObject;
import io.luck.DotaTrainerBackend.providers.opendota.service.OpenDotaPlayerMatchDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MatchService extends BaseService {

    private final HeroService heroService;
    private final MatchDetailsMapper matchDetailsMapper;
    private final OpenDotaPlayerMatchDataService openDotaPlayerMatchDataService;

    public MatchService(HeroService heroService, MatchDetailsMapper matchDetailsMapper, OpenDotaPlayerMatchDataService openDotaPlayerMatchDataService) {
        this.heroService = heroService;
        this.matchDetailsMapper = matchDetailsMapper;
        this.openDotaPlayerMatchDataService = openDotaPlayerMatchDataService;
    }

    public List<MatchSummary> getRecentMatches(Long playerId) {

        var recentMatches = openDotaPlayerMatchDataService.getPlayerRecentMatches(playerId);

        if (recentMatches == null || recentMatches.isEmpty())
            return Collections.emptyList();

        var heroesIds = recentMatches.stream()
                .map(OpenDotaPlayerMatch::heroId)
                .collect(Collectors.toSet());

        List<Hero> heroes = heroService.getAllById(heroesIds);
        Map<Integer, Hero> heroMap = heroes.stream()
                .collect(Collectors.toMap(
                        Hero::getId,
                        Function.identity()
                ));

        return recentMatches.stream()
                .map(match -> MatchSummary.from(
                        match,
                        heroMap.get(match.heroId())
                ))
                .toList();
    }

    public Map<String, Integer> getWinLose(Long playerId) {
        Map<String, Integer> result = new HashMap<>();
        String uri = this.apiUrl + "players/" + playerId + "/wl";
        ResponseEntity<String> totalWL = this.getFromApi(uri);
        JSONObject wl = new JSONObject(totalWL.getBody());
        result.put("win", wl.getInt("win"));
        result.put("lose", wl.getInt("lose"));
        return result;
    }

    public Match getMatchDetails(Long matchId) {
        String uri = this.apiUrl + "matches/" + matchId;
        ResponseEntity<String> matchFromApi = this.getFromApi(uri);
        JSONObject matchObj = new JSONObject(matchFromApi.getBody());
        return matchDetailsMapper.convert(matchObj);
    }

}
