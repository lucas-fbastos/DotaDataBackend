package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.domain.Match;
import com.br.app.DotaTrainerBackend.domain.MatchSummary;

import com.br.app.DotaTrainerBackend.mapper.MatchDetailsMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class MatchService extends BaseService {

    @Autowired
    private HeroService heroService;
    @Autowired
    private MatchDetailsMapper matchDetailsMapper;

    private final Logger LOGGER = Logger.getLogger(MatchService.class.getName());

    public List<MatchSummary> getRecentMatches(Long playerId){
        String uri = this.apiUrl+"players/"+playerId+"/recentMatches";
        ResponseEntity<String> matchesFromAPI = this.getFromApi(uri);
        JSONArray matchesOBJ = new JSONArray(matchesFromAPI.getBody());
        List<MatchSummary> recentMatches = new ArrayList<>();
        matchesOBJ.forEach(match ->{
            JSONObject matchJson = (JSONObject) match;
            MatchSummary summary = new MatchSummary(matchJson);
            summary.setHero( heroService.getHeroById(matchJson.getInt("hero_id")));
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
