package com.br.app.DotaTrainerBackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.json.JSONObject;

@Entity
@Table(schema = "public",name = "pro_player")
public class ProPlayer extends Player{

    private String teamName;
    private String teamTag;
    private Integer playerRole;
    private Long teamId;
    private Boolean isLocked;
    private Long lockedUntil;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamTag() {
        return teamTag;
    }

    public void setTeamTag(String teamTag) {
        this.teamTag = teamTag;
    }

    public Integer getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(Integer playerRole) {
        this.playerRole = playerRole;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public Long getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(Long lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public ProPlayer(){
        super();
    }

    public ProPlayer(JSONObject p){
        super(p);
        this.playerRole = p.optInt("fantasy_role");
        this.lockedUntil = p.optLong("locked_until");
        this.teamName = p.optString("team_name");
        this.teamId = p.optLong("team_id");
        this.teamTag = p.optString("team_tag");
    }


}
