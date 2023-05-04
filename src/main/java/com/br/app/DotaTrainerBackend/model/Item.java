package com.br.app.DotaTrainerBackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="item",schema = "public")
public class Item {

    @Id
    private Long id;
    private String name;
    private Integer cost;
    private String image;
    private Integer cd;
    private Integer charges;
    private boolean created;
    @OneToMany(mappedBy="item")
    @Fetch(FetchMode.SUBSELECT)
    private List<ItemAttribute> attributes = new ArrayList<>();
    private String quality;
    private String notes;
    @ManyToMany(mappedBy = "composes",cascade = CascadeType.ALL)
    @JoinTable(name="item_composition",
    joinColumns = {@JoinColumn(name="parentId")},
    inverseJoinColumns = {@JoinColumn(name = "id")})
    private List<Item> components = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="item_composition",
            joinColumns = {@JoinColumn(name="id")},
            inverseJoinColumns = {@JoinColumn(name = "parentId")})
    private List<Item> composes = new ArrayList<>();

    private String lore;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCd() {
        return cd;
    }

    public void setCd(Integer cd) {
        this.cd = cd;
    }

    public Integer getCharges() {
        return charges;
    }

    public void setCharges(Integer charges) {
        this.charges = charges;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public List<ItemAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ItemAttribute> attributes) {
        this.attributes = attributes;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Item> getComponents() {
        return components;
    }

    public void setComponents(List<Item> components) {
        this.components = components;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public List<Item> getComposes() {
        return composes;
    }

    public void setComposes(List<Item> composes) {
        this.composes = composes;
    }

    public Item(){   }

    public Item(Long id, String name, Integer cost, String image, Integer cd, Integer charges, boolean created, List<ItemAttribute> attributes, String quality, String notes, List<Item> components, List<Item> composes, String lore) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.image = image;
        this.cd = cd;
        this.charges = charges;
        this.created = created;
        this.attributes = attributes;
        this.quality = quality;
        this.notes = notes;
        this.components = components;
        this.composes = composes;
        this.lore = lore;
    }


    public Item(JSONObject json){
        this.name = json.optString("dname");
        this.lore = json.optString("lore");
        this.cost = json.optInt("cost");
        String charges = json.get("charges").toString();
        if(!charges.equals("false")){
            this.charges = Integer.parseInt(charges);
        }else{
            this.charges = 0;
        }
        this.created = json.optBoolean("created");
        this.cd = json.optInt("cd");
        this.image = json.optString("img");
        this.notes = json.optString("notes");
        this.quality = json.optString("qual");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;

        if (created != item.created) return false;
        if (!Objects.equals(id, item.id)) return false;
        if (!Objects.equals(name, item.name)) return false;
        if (!Objects.equals(cost, item.cost)) return false;
        if (!Objects.equals(image, item.image)) return false;
        if (!Objects.equals(cd, item.cd)) return false;
        if (!Objects.equals(charges, item.charges)) return false;
        if (!Objects.equals(attributes, item.attributes)) return false;
        if (!Objects.equals(quality, item.quality)) return false;
        if (!Objects.equals(notes, item.notes)) return false;
        if (!Objects.equals(components, item.components)) return false;
        if (!Objects.equals(composes, item.composes)) return false;
        return Objects.equals(lore, item.lore);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (cd != null ? cd.hashCode() : 0);
        result = 31 * result + (charges != null ? charges.hashCode() : 0);
        result = 31 * result + (created ? 1 : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (quality != null ? quality.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (components != null ? components.hashCode() : 0);
        result = 31 * result + (composes != null ? composes.hashCode() : 0);
        result = 31 * result + (lore != null ? lore.hashCode() : 0);
        return result;
    }
}
