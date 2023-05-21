package com.br.app.DotaTrainerBackend.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchPlayer {

    private String team;
    private List<Integer> abilityUpgrade = new ArrayList<>();
    private HashMap<String,String> abilityUses = new HashMap<>();
    private HashMap<String,String> abilityTargets = new HashMap<>();
    private HashMap<String,String> damageTargets = new HashMap<>();
    private Integer assists;
    private Integer deaths;
    private Integer kills;
    private Integer denies;
    private Integer lastHits;
    private Integer level;
    private Integer pings;
    private Integer stuns;
    private Integer tpsBought;
    private Integer towerDamage;
    private Integer sentryUses;
    private Integer observerUses;
    private Long gold;
    private Integer goldPerMin;
    private Integer xpPerMin;
    private Integer goldSpent;
    private Integer heroDamage;
    private Integer heroHeal;
    private Hero hero;
    private String name;
    private String personName;
    private boolean won;
    private String rank;

    private List<Integer> backpackItemsId = new ArrayList<>();
    private HashMap<String,Integer> buybackLog = new HashMap<>();
    private Integer campsStacked;
    private HashMap<String,String> damage = new HashMap<>();
    private HashMap<String,String> damageTaken = new HashMap<>();
}
