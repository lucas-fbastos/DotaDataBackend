package io.luck.DotaTrainerBackend.model;

import io.luck.DotaTrainerBackend.model.entity.Hero;

public class HeroStats {

    private Hero hero;
    private Long games;
    private Long win;
    private Long withGames;
    private Long withWin;
    private Long againstGames;
    private Long againstWin;

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Long getGames() {
        return games;
    }

    public void setGames(Long games) {
        this.games = games;
    }

    public Long getWin() {
        return win;
    }

    public void setWin(Long win) {
        this.win = win;
    }

    public Long getWithGames() {
        return withGames;
    }

    public void setWithGames(Long withGames) {
        this.withGames = withGames;
    }

    public Long getAgainstGames() {
        return againstGames;
    }

    public void setAgainstGames(Long againstGames) {
        this.againstGames = againstGames;
    }

    public Long getAgainstWin() {
        return againstWin;
    }

    public void setAgainstWin(Long againstWin) {
        this.againstWin = againstWin;
    }

    public Long getWithWin() {
        return withWin;
    }

    public void setWithWin(Long withWin) {
        this.withWin = withWin;
    }

    public HeroStats() { }

    public HeroStats(Hero hero, Long games, Long win, Long withGames, Long withWin ,Long againstGames, Long againstWin) {
        this.hero = hero;
        this.games = games;
        this.win = win;
        this.withGames = withGames;
        this.withWin = withWin;
        this.againstGames = againstGames;
        this.againstWin = againstWin;
    }
}
