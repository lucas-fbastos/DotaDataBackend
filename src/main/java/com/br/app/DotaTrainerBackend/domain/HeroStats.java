package com.br.app.DotaTrainerBackend.domain;

public class HeroStats {

    private Hero hero;
    private Integer games;
    private Integer win;
    private Integer withGames;
    private Integer againstGames;
    private Integer againstWins;

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Integer getGames() {
        return games;
    }

    public void setGames(Integer games) {
        this.games = games;
    }

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }

    public Integer getWithGames() {
        return withGames;
    }

    public void setWithGames(Integer withGames) {
        this.withGames = withGames;
    }

    public Integer getAgainstGames() {
        return againstGames;
    }

    public void setAgainstGames(Integer againstGames) {
        this.againstGames = againstGames;
    }

    public Integer getAgainstWins() {
        return againstWins;
    }

    public void setAgainstWins(Integer againstWins) {
        this.againstWins = againstWins;
    }

    public HeroStats() { }

    public HeroStats(Hero hero, Integer games, Integer win, Integer withGames, Integer againstGames, Integer againstWins) {
        this.hero = hero;
        this.games = games;
        this.win = win;
        this.withGames = withGames;
        this.againstGames = againstGames;
        this.againstWins = againstWins;
    }
}
