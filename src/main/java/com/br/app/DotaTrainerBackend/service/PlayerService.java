package com.br.app.DotaTrainerBackend.service;


import com.br.app.DotaTrainerBackend.domain.Player;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
}
