package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.domain.Hero;
import com.br.app.DotaTrainerBackend.domain.Match;
import com.br.app.DotaTrainerBackend.domain.MatchSummary;
import com.br.app.DotaTrainerBackend.mapper.MatchDetailsMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchService extends BaseService {

    @Autowired
    private HeroService heroService;
    @Autowired
    private MatchDetailsMapper matchDetailsMapper;


    public List<MatchSummary> getRecentMatches(Long playerId){
        String uri = this.apiUrl+"players/"+playerId+"/recentMatches";
        ResponseEntity<String> matchesFromAPI = this.getFromApi(uri);
        JSONArray matchesOBJ = new JSONArray(matchesFromAPI.getBody());
        List<MatchSummary> recentMatches = new ArrayList<>();
        Set<Integer> heroesId = matchesOBJ.toList().stream().map(match -> {
            try {
                JSONObject matchObj = new JSONObject(new ObjectMapper().writeValueAsString(match));
                return matchObj.getInt("hero_id");
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toSet());

        List<Hero> heroes = heroService.getAllById(heroesId);
        Map<Integer,Hero> heroMap = new HashMap<>();
        heroes.forEach(hero ->{
            heroMap.put(hero.getId(),hero);
        });

        matchesOBJ.forEach(match ->{
            JSONObject matchJson = (JSONObject) match;
            MatchSummary summary = new MatchSummary(matchJson);
            summary.setHero( heroMap.get(matchJson.getInt("hero_id")));
            recentMatches.add(summary);
        });
        return recentMatches;
    }


    public Map<String,Integer> getWinLose(Long playerId){
        Map<String,Integer> result = new HashMap<>();
        String uri = this.apiUrl+"players/"+playerId+"/wl";
        ResponseEntity<String> totalWL = this.getFromApi(uri);
        JSONObject wl = new JSONObject(totalWL.getBody());
        result.put("win",wl.getInt("win"));
        result.put("lose",wl.getInt("lose"));
        return result;
    }

    public Match getMatchDetails(Long matchId){
        String uri = this.apiUrl+"matches/"+matchId;
        ResponseEntity<String> matchFromApi = this.getFromApi(uri);
        JSONObject matchObj = new JSONObject(matchFromApi.getBody());
        return matchDetailsMapper.convert(matchObj);
    }

}
