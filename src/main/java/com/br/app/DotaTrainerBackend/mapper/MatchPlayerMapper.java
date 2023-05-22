package com.br.app.DotaTrainerBackend.mapper;

import com.br.app.DotaTrainerBackend.domain.Hero;
import com.br.app.DotaTrainerBackend.domain.Item;
import com.br.app.DotaTrainerBackend.domain.MatchPlayer;
import com.br.app.DotaTrainerBackend.service.HeroService;
import com.br.app.DotaTrainerBackend.service.ItemService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatchPlayerMapper implements Mapper<MatchPlayer> {

    @Autowired
    private HeroService heroService;
    @Autowired
    private ItemService itemService;

    @Override
    public MatchPlayer convert(JSONObject json) {
        MatchPlayer matchPlayer = new MatchPlayer();
        matchPlayer.setKills(json.getInt("kills"));
        matchPlayer.setAssists(json.getInt("assists"));
        matchPlayer.setDeaths(json.getInt("deaths"));
        matchPlayer.setDenies(json.getInt("denies"));
        matchPlayer.setLastHits(json.getInt("last_hits"));
        matchPlayer.setGold(json.getLong("gold"));
        matchPlayer.setGoldSpent(json.getLong("gold_spent"));
        matchPlayer.setGoldPerMin(json.getInt("gold_per_min"));
        matchPlayer.setXpPerMin(json.getInt("xp_per_min"));
        Hero hero = heroService.getHeroById(json.getInt("hero_id"));
        matchPlayer.setHero(hero);
        matchPlayer.setItems(processItems(json));
        boolean isRadiant = json.getBoolean("isRadiant");
        boolean radiantWin = json.getBoolean("radiant_win");
        if (isRadiant) {
            matchPlayer.setTeam("Radiant");
            matchPlayer.setWon(radiantWin);
        } else {
            matchPlayer.setTeam("Dire");
            matchPlayer.setWon(!radiantWin);
        }
        matchPlayer.setHeroHeal(json.getInt("hero_healing"));
        matchPlayer.setHeroDamage(json.getInt("hero_damage"));
        return matchPlayer;
    }

    private List<Item> processItems(JSONObject json) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            long id = json.getLong("item_"+i);
            if(id>0){
                Item item = itemService.getById(id);
                items.add(item);
            }else {
                items.add(null);
            }
        }
        return items;
    }
}
