package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.model.MatchSummary;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService extends BaseService {

    @Autowired
    private HeroService heroService;

    public List<MatchSummary> getRecentMatches(Long playerId){
        String matchesFromAPI = this.getFromApi(this.apiUrl+"/player/"+playerId+"/recentMatches");
        JSONArray matchesOBJ = new JSONArray(matchesFromAPI);
        List<MatchSummary> recentMatches = new ArrayList<>();
        matchesOBJ.forEach(match ->{
            JSONObject matchJson = (JSONObject) match;
            MatchSummary summary = new MatchSummary(matchJson);
            //TODO get hero from cache with its id, then set it here summary.setHero();
            recentMatches.add(summary);
        });
        return recentMatches;
    }


}
