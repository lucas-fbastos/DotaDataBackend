package com.br.app.DotaTrainerBackend.service;


import com.br.app.DotaTrainerBackend.model.Player;
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
        int statusCode = playerFromApi.getStatusCode().value();
        if(statusCode!=200){
            LOGGER.warning("URI: "+uri+" RETURNED: "+statusCode);
            return null;
        }
        JSONObject playerJson = new JSONObject(playerFromApi);
        return new Player(playerJson);
    }
}
