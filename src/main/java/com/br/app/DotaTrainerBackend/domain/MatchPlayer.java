package com.br.app.DotaTrainerBackend.domain;

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
    private Long goldSpent;
    private Integer heroDamage;
    private Integer heroHeal;
    private Hero hero;
    private String name;
    private String personaName;
    private boolean won;
    private String rank;
    private List<Item> items = new ArrayList<>();
    private List<Integer> backpackItemsId = new ArrayList<>();
    private HashMap<String,Integer> buybackLog = new HashMap<>();
    private Integer campsStacked;
    private HashMap<String,String> damage = new HashMap<>();
    private HashMap<String,String> damageTaken = new HashMap<>();

    public MatchPlayer() {    }

    public MatchPlayer(String team, List<Integer> abilityUpgrade, HashMap<String, String> abilityUses,
                       HashMap<String, String> abilityTargets, HashMap<String, String> damageTargets, Integer assists,
                       Integer deaths, Integer kills, Integer denies, Integer lastHits, Integer level, Integer pings,
                       Integer stuns, Integer tpsBought, Integer towerDamage, Integer sentryUses, Integer observerUses,
                       Long gold, Integer goldPerMin, Integer xpPerMin, Long goldSpent, Integer heroDamage,
                       Integer heroHeal, Hero hero, String name, String personName, boolean won, String rank,
                       List<Item> items, List<Integer> backpackItemsId, HashMap<String, Integer> buybackLog,
                       Integer campsStacked, HashMap<String, String> damage, HashMap<String, String> damageTaken) {
        this.team = team;
        this.abilityUpgrade = abilityUpgrade;
        this.abilityUses = abilityUses;
        this.abilityTargets = abilityTargets;
        this.damageTargets = damageTargets;
        this.assists = assists;
        this.deaths = deaths;
        this.kills = kills;
        this.denies = denies;
        this.lastHits = lastHits;
        this.level = level;
        this.pings = pings;
        this.stuns = stuns;
        this.tpsBought = tpsBought;
        this.towerDamage = towerDamage;
        this.sentryUses = sentryUses;
        this.observerUses = observerUses;
        this.gold = gold;
        this.goldPerMin = goldPerMin;
        this.xpPerMin = xpPerMin;
        this.goldSpent = goldSpent;
        this.heroDamage = heroDamage;
        this.heroHeal = heroHeal;
        this.hero = hero;
        this.name = name;
        this.personaName = personName;
        this.won = won;
        this.rank = rank;
        this.items = items;
        this.backpackItemsId = backpackItemsId;
        this.buybackLog = buybackLog;
        this.campsStacked = campsStacked;
        this.damage = damage;
        this.damageTaken = damageTaken;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public List<Integer> getAbilityUpgrade() {
        return abilityUpgrade;
    }

    public void setAbilityUpgrade(List<Integer> abilityUpgrade) {
        this.abilityUpgrade = abilityUpgrade;
    }

    public HashMap<String, String> getAbilityUses() {
        return abilityUses;
    }

    public void setAbilityUses(HashMap<String, String> abilityUses) {
        this.abilityUses = abilityUses;
    }

    public HashMap<String, String> getAbilityTargets() {
        return abilityTargets;
    }

    public void setAbilityTargets(HashMap<String, String> abilityTargets) {
        this.abilityTargets = abilityTargets;
    }

    public HashMap<String, String> getDamageTargets() {
        return damageTargets;
    }

    public void setDamageTargets(HashMap<String, String> damageTargets) {
        this.damageTargets = damageTargets;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getDenies() {
        return denies;
    }

    public void setDenies(Integer denies) {
        this.denies = denies;
    }

    public Integer getLastHits() {
        return lastHits;
    }

    public void setLastHits(Integer lastHits) {
        this.lastHits = lastHits;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPings() {
        return pings;
    }

    public void setPings(Integer pings) {
        this.pings = pings;
    }

    public Integer getStuns() {
        return stuns;
    }

    public void setStuns(Integer stuns) {
        this.stuns = stuns;
    }

    public Integer getTpsBought() {
        return tpsBought;
    }

    public void setTpsBought(Integer tpsBought) {
        this.tpsBought = tpsBought;
    }

    public Integer getTowerDamage() {
        return towerDamage;
    }

    public void setTowerDamage(Integer towerDamage) {
        this.towerDamage = towerDamage;
    }

    public Integer getSentryUses() {
        return sentryUses;
    }

    public void setSentryUses(Integer sentryUses) {
        this.sentryUses = sentryUses;
    }

    public Integer getObserverUses() {
        return observerUses;
    }

    public void setObserverUses(Integer observerUses) {
        this.observerUses = observerUses;
    }

    public Long getGold() {
        return gold;
    }

    public void setGold(Long gold) {
        this.gold = gold;
    }

    public Integer getGoldPerMin() {
        return goldPerMin;
    }

    public void setGoldPerMin(Integer goldPerMin) {
        this.goldPerMin = goldPerMin;
    }

    public Integer getXpPerMin() {
        return xpPerMin;
    }

    public void setXpPerMin(Integer xpPerMin) {
        this.xpPerMin = xpPerMin;
    }

    public Long getGoldSpent() {
        return goldSpent;
    }

    public void setGoldSpent(Long goldSpent) {
        this.goldSpent = goldSpent;
    }

    public Integer getHeroDamage() {
        return heroDamage;
    }

    public void setHeroDamage(Integer heroDamage) {
        this.heroDamage = heroDamage;
    }

    public Integer getHeroHeal() {
        return heroHeal;
    }

    public void setHeroHeal(Integer heroHeal) {
        this.heroHeal = heroHeal;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonaName() {
        return personaName;
    }

    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Integer> getBackpackItemsId() {
        return backpackItemsId;
    }

    public void setBackpackItemsId(List<Integer> backpackItemsId) {
        this.backpackItemsId = backpackItemsId;
    }

    public HashMap<String, Integer> getBuybackLog() {
        return buybackLog;
    }

    public void setBuybackLog(HashMap<String, Integer> buybackLog) {
        this.buybackLog = buybackLog;
    }

    public Integer getCampsStacked() {
        return campsStacked;
    }

    public void setCampsStacked(Integer campsStacked) {
        this.campsStacked = campsStacked;
    }

    public HashMap<String, String> getDamage() {
        return damage;
    }

    public void setDamage(HashMap<String, String> damage) {
        this.damage = damage;
    }

    public HashMap<String, String> getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(HashMap<String, String> damageTaken) {
        this.damageTaken = damageTaken;
    }
}
