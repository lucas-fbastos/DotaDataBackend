package com.br.app.DotaTrainerBackend.service;


import com.br.app.DotaTrainerBackend.domain.HeroStats;
import com.br.app.DotaTrainerBackend.domain.Player;
import org.hibernate.cfg.NotYetImplementedException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PlayerService extends BaseService {

    private static final Logger LOGGER = Logger.getLogger(PlayerService.class.getName());

    public Player getPlayerById(Long playerId){
        String uri = this.apiUrl+"players/"+playerId;
        ResponseEntity<String> playerFromApi = this.getFromApi(uri);
        LOGGER.info("return from "+ uri+": "+playerFromApi.getBody());
        JSONObject playerJson = new JSONObject(playerFromApi.getBody());
        return new Player(playerJson);
    }

    public List<HeroStats> getPlayerHeroStatus(long playerId){
        String uri = this.apiUrl+"players/"+playerId+"/heroes?significant=1&limit=20&patch=7.32&days=30";
        ResponseEntity<String> heroStatsFromApi = this.getFromApi(uri);
        LOGGER.info("return from "+ uri+": "+heroStatsFromApi.getBody());
        return convert(heroStatsFromApi.getBody());
    }

    private List<HeroStats> convert(String body) {
        List<HeroStats> list = new ArrayList<>();
        throw new NotYetImplementedException();
    }
}
