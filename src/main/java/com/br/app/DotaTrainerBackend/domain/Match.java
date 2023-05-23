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
    private Set<MatchPlayer> radiantTeam;
    private Set<MatchPlayer> direTeam;
    private String replayUrl;

    public Match() {    }

    public Match(long matchId, int cluster, GameMode gameMode, Integer direScore, Integer radiantScore,
                 Duration duration, boolean radiantWin, Set<MatchPlayer> radiantTeam, Set<MatchPlayer> direTeam,
                 String replayUrl) {
        this.matchId = matchId;
        this.cluster = cluster;
        this.gameMode = gameMode;
        this.direScore = direScore;
        this.radiantScore = radiantScore;
        this.duration = duration;
        this.radiantWin = radiantWin;
        this.radiantTeam = radiantTeam;
        this.direTeam = direTeam;
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

    public Set<MatchPlayer> getRadiantTeam() {
        return radiantTeam;
    }

    public void setRadiantTeam(Set<MatchPlayer> radiantTeam) {
        this.radiantTeam = radiantTeam;
    }

    public Set<MatchPlayer> getDireTeam() {
        return direTeam;
    }

    public void setDireTeam(Set<MatchPlayer> direTeam) {
        this.direTeam = direTeam;
    }

    public String getReplayUrl() {
        return replayUrl;
    }

    public void setReplayUrl(String replayUrl) {
        this.replayUrl = replayUrl;
    }
}
