package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.model.MatchSummary;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class MatchService extends BaseService {

    @Autowired
    private HeroService heroService;

    private final Logger LOGGER = Logger.getLogger(MatchService.class.getName());

    public List<MatchSummary> getRecentMatches(Long playerId){
        String uri = this.apiUrl+"players/"+playerId+"/recentMatches";
        LOGGER.warning("URI: "+uri);
        ResponseEntity<String> matchesFromAPI = this.getFromApi(uri);
        int statusCode = matchesFromAPI.getStatusCode().value();
        if(statusCode!=200){
            LOGGER.warning("API RETURNED "+statusCode);
            return new ArrayList<>();
        }
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


}
