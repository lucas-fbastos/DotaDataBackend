package io.luck.AghsLabs.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.luck.AghsLabs.model.Enums.Faction;

import java.util.List;

public record OpenDotaMatchPlayer(
        Integer kills,
        Integer assists,
        Integer deaths,
        Integer denies,
        @JsonProperty("last_hits")
        Integer lastHits,
        Long gold,
        @JsonProperty("gold_spent")
        Long goldSpent,
        @JsonProperty("gold_per_min")
        Integer goldPerMin,
        @JsonProperty("xp_per_min")
        Integer xpPerMin,
        @JsonProperty("hero_id")
        Long heroId,
        String name,
        Integer level,
        @JsonProperty("personaname")
        String personaName,
        Boolean isRadiant,
        @JsonProperty("radiant_win")
        Boolean radiantWin,
        @JsonProperty("item_1")
        Long item1,
        @JsonProperty("item_2")
        Long item2,
        @JsonProperty("item_3")
        Long item3,
        @JsonProperty("item_4")
        Long item4,
        @JsonProperty("item_5")
        Long item5,
        @JsonProperty("item_6")
        Long item6,
        @JsonProperty("hero_heal")
        Long heroHeal,
        @JsonProperty("hero_damage")
        Long heroDamage
        ){

        public List<Long> getItems(){
                return List.of(item1, item2, item3, item4, item5, item6);
        }

        public Faction getTeam(){
                if(isRadiant) return Faction.RADIANT;
                else return Faction.DIRE;
        }

        public Boolean playerWon(){
                var team = getTeam();
                return team.won(radiantWin);
        }
}
