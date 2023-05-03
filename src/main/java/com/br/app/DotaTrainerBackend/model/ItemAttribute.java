package com.br.app.DotaTrainerBackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name="item_attribute",schema = "public")
public class ItemAttribute {
    @Id
    private Long Id;
    private String key;
    private String header;
    private String value;
    private String footer;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public ItemAttribute(Long id, String key, String header, String value, String footer) {
        Id = id;
        this.key = key;
        this.header = header;
        this.value = value;
        this.footer = footer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemAttribute that)) return false;

        if (!Objects.equals(Id, that.Id)) return false;
        if (!Objects.equals(key, that.key)) return false;
        if (!Objects.equals(header, that.header)) return false;
        if (!Objects.equals(value, that.value)) return false;
        return Objects.equals(footer, that.footer);
    }

    @Override
    public int hashCode() {
        int result = Id != null ? Id.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (header != null ? header.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (footer != null ? footer.hashCode() : 0);
        return result;
    }
}
