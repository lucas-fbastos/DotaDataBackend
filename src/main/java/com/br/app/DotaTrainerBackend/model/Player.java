package com.br.app.DotaTrainerBackend.model;

import jakarta.persistence.*;
import org.json.JSONObject;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name="player", schema ="public")
public class Player {

    @Id
    private Long accountId;
    private Integer soloRank;
    private Integer rankTier;
    private Integer competitiveRank;
    private Integer leaderboardRank;
    private Integer mmrEstimate;
    private String personName;
    private String name;
    private boolean plus;
    private Integer cheese;

    private Long steamId;

    private String avatarImage;

    private String avatarImageMedium;

    private String avatarImageFull;

    private String profileURL;

    private String lastLogin;

    private String locCountryCode;

    private Boolean contributor;

    private Boolean subscriber;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Integer getSoloRank() {
        return soloRank;
    }

    public void setSoloRank(Integer soloRank) {
        this.soloRank = soloRank;
    }

    public Integer getRankTier() {
        return rankTier;
    }

    public void setRankTier(Integer rankTier) {
        this.rankTier = rankTier;
    }

    public Integer getLeaderboardRank() {
        return leaderboardRank;
    }

    public void setLeaderboardRank(Integer leaderboardRank) {
        this.leaderboardRank = leaderboardRank;
    }

    public Integer getMmrEstimate() {
        return mmrEstimate;
    }

    public void setMmrEstimate(Integer mmrEstimate) {
        this.mmrEstimate = mmrEstimate;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPlus() {
        return plus;
    }

    public void setPlus(boolean plus) {
        this.plus = plus;
    }

    public Integer getCheese() {
        return cheese;
    }

    public void setCheese(Integer cheese) {
        this.cheese = cheese;
    }

    public Long getSteamId() {
        return steamId;
    }

    public void setSteamId(Long steamId) {
        this.steamId = steamId;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public String getAvatarImageMedium() {
        return avatarImageMedium;
    }

    public void setAvatarImageMedium(String avatarImageMedium) {
        this.avatarImageMedium = avatarImageMedium;
    }

    public String getAvatarImageFull() {
        return avatarImageFull;
    }

    public void setAvatarImageFull(String avatarImageFull) {
        this.avatarImageFull = avatarImageFull;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLocCountryCode() {
        return locCountryCode;
    }

    public void setLocCountryCode(String locCountryCode) {
        this.locCountryCode = locCountryCode;
    }

    public Boolean getContributor() {
        return contributor;
    }

    public void setContributor(Boolean contributor) {
        this.contributor = contributor;
    }

    public Boolean getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Boolean subscriber) {
        this.subscriber = subscriber;
    }

    public void setCompetitiveRank(Integer competitiveRank){
        this.competitiveRank = competitiveRank;
    }

    public Integer getCompetitiveRank(){
        return this.competitiveRank;
    }

    public Player(){ }

    public Player(JSONObject playerJSON){
        this.soloRank = playerJSON.optInt("solo_competitive_rank");
        this.leaderboardRank = playerJSON.optInt("leaderboard_rank");
        this.rankTier = playerJSON.optInt("rank_tier");
        this.competitiveRank = playerJSON.optInt("competitive_rank");

        JSONObject mmrEstimate = playerJSON.optJSONObject("mmr_estimate");
        if(mmrEstimate!=null)
            this.mmrEstimate = mmrEstimate.optInt("estimate");

        JSONObject profile = playerJSON.optJSONObject("profile");
        if(profile!=null){
            this.accountId = profile.optLong("account_id");
            this.personName = profile.optString("personname");
            this.name = profile.optString("name");
            this.plus = profile.optBoolean("plus");
            this.cheese = profile.optInt("cheese");
            this.steamId = profile.optLong("steamid");
            this.avatarImage = profile.optString("avatar");
            this.avatarImageMedium = profile.optString("avatarmedium");
            this.avatarImageFull = profile.optString("avatarfull");
            this.profileURL = profile.optString("profileurl");
            this.lastLogin = profile.optString("last_login");
            this.locCountryCode = profile.optString("loccountrycode");
            this.contributor = profile.optBoolean("is_contributor");
            this.subscriber = profile.optBoolean("is_subscriber");
        }

    }

    public Player(long accountId, Integer soloRank, Integer rankTier, Integer leaderboardRank, Integer mmrEstimate, String personName, String name, boolean plus, Integer chesse, Long steamId, String avatarImage, String avatarImageMedium, String avatarImageFull, String profileURL, String lastLogin, String locCountryCode, Boolean contributor, Boolean subscriber) {
        this.accountId = accountId;
        this.soloRank = soloRank;
        this.rankTier = rankTier;
        this.leaderboardRank = leaderboardRank;
        this.mmrEstimate = mmrEstimate;
        this.personName = personName;
        this.name = name;
        this.plus = plus;
        this.cheese = chesse;
        this.steamId = steamId;
        this.avatarImage = avatarImage;
        this.avatarImageMedium = avatarImageMedium;
        this.avatarImageFull = avatarImageFull;
        this.profileURL = profileURL;
        this.lastLogin = lastLogin;
        this.locCountryCode = locCountryCode;
        this.contributor = contributor;
        this.subscriber = subscriber;
    }
}
