package com.br.app.DotaTrainerBackend.service;


import com.br.app.DotaTrainerBackend.domain.Hero;
import com.br.app.DotaTrainerBackend.domain.HeroStats;
import com.br.app.DotaTrainerBackend.domain.Player;
import org.hibernate.cfg.NotYetImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PlayerService extends BaseService {

    private static final Logger LOGGER = Logger.getLogger(PlayerService.class.getName());

    @Autowired
    private HeroService heroService;

    public Player getPlayerById(Long playerId){
        String uri = this.apiUrl+"players/"+playerId;
        ResponseEntity<String> playerFromApi = this.getFromApi(uri);
        LOGGER.info("return from "+ uri+": "+playerFromApi.getBody());
        JSONObject playerJson = new JSONObject(playerFromApi.getBody());
        return new Player(playerJson);
    }

    public List<HeroStats> getPlayerHeroStatus(long playerId){
        String uri = this.apiUrl+"players/"+playerId+"/heroes?significant=0&days=60";
        ResponseEntity<String> heroStatsFromApi = this.getFromApi(uri);
        LOGGER.info("return from "+ uri+": "+heroStatsFromApi.getBody());
        return convert(heroStatsFromApi.getBody());
    }

    private List<HeroStats> convert(String body) {
        List<HeroStats> list = new ArrayList<>();
        JSONArray json = new JSONArray(body);
        json.forEach(x  -> {
            JSONObject object = (JSONObject) x;
            Hero hero = heroService.getHeroById(object.getInt("hero_id"));
            HeroStats heroStats = new HeroStats(hero,
                    object.getInt("games"),
                    object.getInt("win"),
                    object.getInt("with_games"),
                    object.getInt("with_win"),
                    object.getInt("against_games"),
                    object.getInt("against_win"));
            list.add(heroStats);
        });

        return list;
    }
}
