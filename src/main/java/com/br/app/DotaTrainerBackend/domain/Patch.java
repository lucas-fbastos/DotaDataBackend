package com.br.app.DotaTrainerBackend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name="patch",schema = "public")
public class Patch {

    @Id
    private Integer id;

    private Instant date;

    private String name;

    public Patch(JSONObject patchJson) {
        this.date = Instant.parse(patchJson.getString("date"));
        this.id = patchJson.getInt("id");
        this.name = patchJson.getString("name");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Patch(){ }

    public Patch(Integer id, Instant date, String name) {
        this.id = id;
        this.date = date;
        this.name = name;
    }
}
