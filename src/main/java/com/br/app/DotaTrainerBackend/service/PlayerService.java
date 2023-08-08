package com.br.app.DotaTrainerBackend.service;


import com.br.app.DotaTrainerBackend.domain.Hero;
import com.br.app.DotaTrainerBackend.domain.HeroStats;
import com.br.app.DotaTrainerBackend.domain.Patch;
import com.br.app.DotaTrainerBackend.domain.Player;
import com.br.app.DotaTrainerBackend.repository.PatchRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PlayerService extends BaseService {

    private static final Logger LOGGER = Logger.getLogger(PlayerService.class.getName());

    @Autowired
    private HeroService heroService;

    @Autowired
    private PatchRepository patchRepository;

    public Player getPlayerById(Long playerId){
        String uri = this.apiUrl+"players/"+playerId;
        ResponseEntity<String> playerFromApi = this.getFromApi(uri);
        JSONObject playerJson = new JSONObject(playerFromApi.getBody());
        return new Player(playerJson);
    }

    public List<HeroStats> getPlayerHeroStatus(long playerId,Integer limit, Integer offset){
        Patch lastPatch = patchRepository.findEntityWithMaxId();
        String uri = this.apiUrl+"players/"+playerId+"/heroes?significant=0&days=60&patch="+lastPatch.getId();
        ResponseEntity<String> heroStatsFromApi = this.getFromApi(uri);
        JSONArray array = new JSONArray(heroStatsFromApi.getBody());
        return convert(filterArray(array,limit,offset));
    }

    private  List<Object> filterArray(JSONArray array, Integer limit, Integer offset) {
        return array.toList().stream().skip(offset).limit(limit).toList();
    }

    private List<HeroStats> convert(List<Object> jsonList) {
        List<HeroStats> list = new ArrayList<>();
        Set<Integer> heroIds = jsonList.stream()
                .map(object -> {
                    try {
                        JSONObject jsonObject = new JSONObject(new ObjectMapper().writeValueAsString(object));
                        return jsonObject.getInt("hero_id");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());

        Map<Integer, Hero> heroMap = heroService.getAllById(heroIds)
                .stream()
                .collect(Collectors.toMap(Hero::getId, Function.identity()));

        jsonList.forEach(object -> {
            try {
                JSONObject jsonObject = new JSONObject(new ObjectMapper().writeValueAsString(object));
                int heroId = jsonObject.getInt("hero_id");
                Hero hero = heroMap.get(heroId);
                if (hero != null) {
                    HeroStats heroStats = new HeroStats(hero,
                            jsonObject.getInt("games"),
                            jsonObject.getInt("win"),
                            jsonObject.getInt("with_games"),
                            jsonObject.getInt("with_win"),
                            jsonObject.getInt("against_games"),
                            jsonObject.getInt("against_win"));
                    list.add(heroStats);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        return list;
    }
}
