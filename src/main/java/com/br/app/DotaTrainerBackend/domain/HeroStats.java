package com.br.app.DotaTrainerBackend.domain;

public class HeroStats {

    private Hero hero;
    private Integer games;
    private Integer win;
    private Integer withGames;
    private Integer withWin;
    private Integer againstGames;
    private Integer againstWin;

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

    public Integer getAgainstWin() {
        return againstWin;
    }

    public void setAgainstWin(Integer againstWin) {
        this.againstWin = againstWin;
    }

    public Integer getWithWin() {
        return withWin;
    }

    public void setWithWin(Integer withWin) {
        this.withWin = withWin;
    }

    public HeroStats() { }

    public HeroStats(Hero hero, Integer games, Integer win, Integer withGames, Integer withWin ,Integer againstGames, Integer againstWin) {
        this.hero = hero;
        this.games = games;
        this.win = win;
        this.withGames = withGames;
        this.withWin = withWin;
        this.againstGames = againstGames;
        this.againstWin = againstWin;
    }
}
