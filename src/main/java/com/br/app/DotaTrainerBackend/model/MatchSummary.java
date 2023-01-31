package com.br.app.DotaTrainerBackend.model;

import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MatchSummary {
                                                
    private Long matchId;
    private boolean isRadiant;
    private boolean playerWon;
    private java.time.Duration Duration;
    private Integer gameMode;
    private Integer lobbyType;
    private Hero hero;
    private LocalDateTime matchStart;
    private Integer version;
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Integer skill;
    private Integer averageRank;
    private Integer xpPerMinute;
    private Integer goldPerMinute;
    private Integer heroDamage;
    private Integer heroHealing;
    private Integer lastHits;
    private Integer lane;
    private Integer laneRole;
    private Boolean isRoaming;
    private Integer cluster;

    private Integer leaverStatus;
    private Integer partySize;

    public MatchSummary() { }

    public MatchSummary(JSONObject match){
        this.matchId    = match.optLong("match_id");
        this.assists    = match.optInt("assists");
        this.deaths     = match.optInt("deaths");
        this.kills      = match.optInt("kills");
        this.skill      = match.optInt("skill");
        this.Duration   = java.time.Duration.ofSeconds(match.optLong("duration"));
        this.matchStart = LocalDateTime.ofEpochSecond(match.optLong("start_time"),
                0, ZoneOffset.UTC);
        this.cluster     = match.optInt("cluster");
        this.averageRank = match.optInt("average_rank");
        this.xpPerMinute = match.optInt("xp_per_min");
        this.goldPerMinute = match.optInt("gold_per_min");
        this.heroDamage = match.optInt("hero_damage");
        this.heroHealing = match.optInt("hero_healing");
        this.lastHits = match.optInt("last_hits");
        this.lane = match.optInt("lane");
        this.laneRole = match.optInt("lane_role");
        this.isRoaming = match.optBoolean("is_roaming");
        this.leaverStatus = match.optInt("leaver_status");
        this.partySize = match.optInt("party_size");
        this.lobbyType = match.optInt("lobby_type");
        this.version = match.optInt("version");
        determineWin(match);
    }

    private void determineWin(JSONObject match){
        int slot = match.optInt("player_slot");
        // players occupying slots from 0 to 127 are radiant, from 128 to 255 are dire.
        this.isRadiant = slot <= 127;
        this.playerWon = this.isRadiant && match.optBoolean("radiant_win");
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getDuration() {
        return Duration.toString();
    }

    public void setDuration(Duration duration) {
        Duration = duration;
    }

    public Integer getGameMode() {
        return gameMode;
    }

    public void setGameMode(Integer gameMode) {
        this.gameMode = gameMode;
    }

    public Integer getLobbyType() {
        return lobbyType;
    }

    public void setLobbyType(Integer lobbyType) {
        this.lobbyType = lobbyType;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public LocalDateTime getMatchStart() {
        return matchStart;
    }

    public void setMatchStart(LocalDateTime matchStart) {
        this.matchStart = matchStart;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getSkill() {
        return skill;
    }

    public void setSkill(Integer skill) {
        this.skill = skill;
    }

    public Integer getAverageRank() {
        return averageRank;
    }

    public void setAverageRank(Integer averageRank) {
        this.averageRank = averageRank;
    }

    public Integer getXpPerMinute() {
        return xpPerMinute;
    }

    public void setXpPerMinute(Integer xpPerMinute) {
        this.xpPerMinute = xpPerMinute;
    }

    public Integer getGoldPerMinute() {
        return goldPerMinute;
    }

    public void setGoldPerMinute(Integer goldPerMinute) {
        this.goldPerMinute = goldPerMinute;
    }

    public Integer getHeroDamage() {
        return heroDamage;
    }

    public void setHeroDamage(Integer heroDamage) {
        this.heroDamage = heroDamage;
    }

    public Integer getHeroHealing() {
        return heroHealing;
    }

    public void setHeroHealing(Integer heroHealing) {
        this.heroHealing = heroHealing;
    }

    public Integer getLastHits() {
        return lastHits;
    }

    public void setLastHits(Integer lastHits) {
        this.lastHits = lastHits;
    }

    public Integer getLane() {
        return lane;
    }

    public void setLane(Integer lane) {
        this.lane = lane;
    }

    public Integer getLaneRole() {
        return laneRole;
    }

    public void setLaneRole(Integer laneRole) {
        this.laneRole = laneRole;
    }

    public Boolean getRoaming() {
        return isRoaming;
    }

    public void setRoaming(Boolean roaming) {
        isRoaming = roaming;
    }

    public Integer getCluster() {
        return cluster;
    }

    public void setCluster(Integer cluster) {
        this.cluster = cluster;
    }

    public String getLeaverStatus() {
       return  switch(this.leaverStatus){
           case 0: yield "Didn't leave";
           case 1: yield "Left safely";
           case 2: yield "Abandoned";
           default: yield "no data";
        };
    }

    public Integer getPartySize() {
        return partySize;
    }

    public void setPartySize(Integer partySize) {
        this.partySize = partySize;
    }
}
