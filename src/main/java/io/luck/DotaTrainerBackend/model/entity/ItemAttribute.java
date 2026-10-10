package io.luck.DotaTrainerBackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@AllArgsConstructor
@EqualsAndHashCode
@Table(name="item_attribute",schema = "public")
public class ItemAttribute {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long Id;
    private String key;
    private String header;
    private String value;
    private String footer;
    @ManyToOne
    @JoinColumn(name="itemId", nullable=false)
    @JsonIgnore
    private Item item;
}
