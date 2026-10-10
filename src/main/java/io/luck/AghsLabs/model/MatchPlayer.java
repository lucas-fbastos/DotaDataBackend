package io.luck.AghsLabs.model;

import io.luck.AghsLabs.model.entity.Hero;
import io.luck.AghsLabs.model.entity.Item;
import lombok.Builder;
import java.util.HashMap;
import java.util.List;

@Builder
public record MatchPlayer(
        String team,
        List<Integer> abilityUpgrade,
        HashMap<String,String> abilityUses,
        HashMap<String,String> abilityTargets,
        HashMap<String,String> damageTargets,
        Integer assists,
        Integer deaths,
        Integer kills,
        Integer denies,
        Integer lastHits,
        Integer level,
        Integer pings,
        Integer stuns,
        Integer tpsBought,
        Integer towerDamage,
        Integer sentryUses,
        Integer observerUses,
        Long gold,
        Integer goldPerMin,
        Integer xpPerMin,
        Long goldSpent,
        Long heroDamage,
        Long heroHeal,
        Hero hero,
        String name,
        String personaName,
        boolean won,
        String rank,
        List<Item> items,
        List<Integer> backpackItemsId,
        HashMap<String,Integer> buybackLog,
        Integer campsStacked,
        HashMap<String,String> damage,
        HashMap<String,String> damageTaken
) {

}
