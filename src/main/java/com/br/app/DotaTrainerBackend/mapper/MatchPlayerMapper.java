package com.br.app.DotaTrainerBackend.mapper;

import com.br.app.DotaTrainerBackend.domain.Hero;
import com.br.app.DotaTrainerBackend.domain.Item;
import com.br.app.DotaTrainerBackend.domain.MatchPlayer;
import com.br.app.DotaTrainerBackend.service.HeroService;
import com.br.app.DotaTrainerBackend.service.ItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MatchPlayerMapper implements Mapper<MatchPlayer> {

    @Autowired
    private HeroService heroService;
    @Autowired
    private ItemService itemService;

    @Override
    public MatchPlayer convert(JSONObject json) {
        return null;
    }

    @Override
    public List<MatchPlayer> convert(JSONArray jsonArray) {
        List<MatchPlayer> players = new ArrayList<>();


        Map<Integer, Hero> heroesMap = getHeroesMap(jsonArray);
        Map<Long, Item> itemsMap = getItemsMap(jsonArray);

        jsonArray.forEach(obj ->{
            JSONObject json = (JSONObject) obj;
            MatchPlayer matchPlayer = new MatchPlayer();
            matchPlayer.setKills(json.getInt("kills"));
            matchPlayer.setAssists(json.getInt("assists"));
            matchPlayer.setDeaths(json.getInt("deaths"));
            matchPlayer.setDenies(json.getInt("denies"));
            matchPlayer.setLastHits(json.getInt("last_hits"));
            matchPlayer.setGold(json.optLong("gold"));
            matchPlayer.setGoldSpent(json.optLong("gold_spent"));
            matchPlayer.setGoldPerMin(json.getInt("gold_per_min"));
            matchPlayer.setXpPerMin(json.getInt("xp_per_min"));
            Hero hero = heroesMap.get(json.getInt("hero_id"));
            matchPlayer.setHero(hero);
            matchPlayer.setItems(processItems(json,itemsMap));
            matchPlayer.setName(json.optString("name"));
            matchPlayer.setLevel(json.getInt("level"));
            matchPlayer.setPersonaName(json.optString("personaname"));
            boolean isRadiant = json.getBoolean("isRadiant");
            boolean radiantWin = json.getBoolean("radiant_win");
            if (isRadiant) {
                matchPlayer.setTeam("Radiant");
                matchPlayer.setWon(radiantWin);
            } else {
                matchPlayer.setTeam("Dire");
                matchPlayer.setWon(!radiantWin);
            }
            matchPlayer.setHeroHeal(json.optInt("hero_healing"));
            matchPlayer.setHeroDamage(json.optInt("hero_damage"));
            players.add(matchPlayer);
        });
        return players;
    }

    private Map<Integer, Hero> getHeroesMap(JSONArray jsonArray){
        Set<Integer> heroesId = jsonArray.toList().stream().map(obj -> {
            try {
                JSONObject json = new JSONObject(new ObjectMapper().writeValueAsString(obj));
                return json.getInt("hero_id");
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }).collect(Collectors.toSet());

        List<Hero> heroes = heroService.getAllById(heroesId);
        Map<Integer,Hero> heroesMap = new HashMap<>();
        heroes.forEach(hero -> heroesMap.put(hero.getId(),hero));
        return heroesMap;
    }

    private Map<Long, Item> getItemsMap(JSONArray jsonArray){
        Set<Long> itemIds = new HashSet<>();
        jsonArray.forEach(obj ->{
            JSONObject json = (JSONObject) obj;
            for (int i = 0; i < 6; i++) {
                long id = json.getLong("item_"+i);
                itemIds.add(id);
            }
        });

        List<Item> items = itemService.getAllById(itemIds);
        Map<Long,Item> itemsMap = new HashMap<>();
        items.forEach(item -> itemsMap.put(item.getItemId(),item));
        return itemsMap;
    }

    private List<Item> processItems(JSONObject json,Map<Long,Item> itemsMap) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            long id = json.getLong("item_"+i);
            if(id>0){
                Item item = itemsMap.get(id);
                items.add(item);
            }else {
                items.add(null);
            }
        }
        return items;
    }
}
