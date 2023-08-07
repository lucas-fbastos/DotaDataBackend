package com.br.app.DotaTrainerBackend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "hero", schema ="public")
public class Hero {

    @Id
    private Integer id;
    private String name;

    private String localizedName;

    private String primaryAttribute;

    private String attackType;

    private String image;

    private String icon;

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(schema = "public")
    private List<Role> roles;

    public Hero(Integer id, String name, String localizedName, String primaryAttribute, String attackType,
                List<Role> roles, String image, String icon) {
        this.id = id;
        this.name = name;
        this.localizedName = localizedName;
        this.primaryAttribute = primaryAttribute;
        this.attackType = attackType;
        this.roles = roles;
        this.image = image;
        this.icon = icon;
    }

    public Hero(JSONObject obj){
        this.id =  obj.optInt("id");
        this.name = obj.optString("name");
        this.localizedName = obj.optString("localized_name");
        this.primaryAttribute =  obj.optString("primary_attr");
        this.attackType = obj.optString("attack_type");
        this.image = obj.optString("img");
        this.icon = obj.optString("icon");
    }

    public Hero() {  }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    public String getPrimaryAttribute() {
        return primaryAttribute;
    }

    public void setPrimaryAttribute(String primaryAttribute) {
        this.primaryAttribute = primaryAttribute;
    }

    public String getAttackType() {
        return attackType;
    }

    public void setAttackType(String attackType) {
        this.attackType = attackType;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hero hero = (Hero) o;

        if (!Objects.equals(id, hero.id)) return false;
        if (!Objects.equals(name, hero.name)) return false;
        if (!Objects.equals(localizedName, hero.localizedName))
            return false;
        if (!Objects.equals(primaryAttribute, hero.primaryAttribute))
            return false;
        if (!Objects.equals(attackType, hero.attackType)) return false;
        return Objects.equals(roles, hero.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, localizedName, primaryAttribute, attackType, image, icon, roles);
    }
}
