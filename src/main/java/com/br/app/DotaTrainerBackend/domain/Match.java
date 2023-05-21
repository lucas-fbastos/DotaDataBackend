package com.br.app.DotaTrainerBackend.domain;

import com.br.app.DotaTrainerBackend.domain.Enums.GameMode;

import java.time.Duration;
import java.util.Set;

public class Match {

    private long matchId;
    private int cluster;
    private GameMode gameMode;
    private Integer direScore;
    private Integer radiantScore;
    private Duration duration;
    private boolean radiantWin;
    private Set<MatchPlayer> players;
    private String replayUrl;

    public Match() {    }

    public Match(long matchId, int cluster, GameMode gameMode, Integer direScore, Integer radiantScore,
                 Duration duration, boolean radiantWin, Set<MatchPlayer> players, String replayUrl) {
        this.matchId = matchId;
        this.cluster = cluster;
        this.gameMode = gameMode;
        this.direScore = direScore;
        this.radiantScore = radiantScore;
        this.duration = duration;
        this.radiantWin = radiantWin;
        this.players = players;
        this.replayUrl = replayUrl;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Integer getDireScore() {
        return direScore;
    }

    public void setDireScore(Integer direScore) {
        this.direScore = direScore;
    }

    public Integer getRadiantScore() {
        return radiantScore;
    }

    public void setRadiantScore(Integer radiantScore) {
        this.radiantScore = radiantScore;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public boolean isRadiantWin() {
        return radiantWin;
    }

    public void setRadiantWin(boolean radiantWin) {
        this.radiantWin = radiantWin;
    }

    public Set<MatchPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(Set<MatchPlayer> players) {
        this.players = players;
    }

    public String getReplayUrl() {
        return replayUrl;
    }

    public void setReplayUrl(String replayUrl) {
        this.replayUrl = replayUrl;
    }
}
