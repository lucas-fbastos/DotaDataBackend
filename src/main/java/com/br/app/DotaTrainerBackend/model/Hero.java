package com.br.app.DotaTrainerBackend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "hero")
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
        this.id = (Integer) obj.get("id");
        this.name = obj.get("name").toString();
        this.localizedName = obj.get("localized_name").toString();
        this.primaryAttribute =  obj.get("primary_attr").toString();
        this.attackType = obj.get("attack_type").toString();
        this.image = obj.get("img").toString();
        this.icon = obj.get("icon").toString();
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

        if (id != hero.id) return false;
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
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (localizedName != null ? localizedName.hashCode() : 0);
        result = 31 * result + (primaryAttribute != null ? primaryAttribute.hashCode() : 0);
        result = 31 * result + (attackType != null ? attackType.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }

}
