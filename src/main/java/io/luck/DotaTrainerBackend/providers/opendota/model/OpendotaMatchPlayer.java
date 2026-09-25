package io.luck.DotaTrainerBackend.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpendotaMatchPlayer(
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
        Long goldPerMin,
        @JsonProperty("xp_per_min")
        Long xpPerMin,
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
        Integer item1,
        @JsonProperty("item_2")
        Integer item2,
        @JsonProperty("item_3")
        Integer item3,
        @JsonProperty("item_4")
        Integer item4,
        @JsonProperty("item_5")
        Integer item5,
        @JsonProperty("item_6")
        Integer item6
){

}
