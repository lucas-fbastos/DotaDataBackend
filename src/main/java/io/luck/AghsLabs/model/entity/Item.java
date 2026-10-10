package io.luck.AghsLabs.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import java.util.List;


@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name="item",schema = "public")
@NoArgsConstructor
public class Item {
    @Id
    @Column(name="item_id")
    private Long itemId;
    private String name;
    private Integer cost;
    private String image;
    private Integer cd;
    private Integer charges;
    private boolean created;
    @OneToMany(mappedBy="item")
    @Fetch(FetchMode.SUBSELECT)
    private List<ItemAttribute> attributes;
    private String quality;
    @Column(columnDefinition="TEXT")
    private String notes;
    @Column(columnDefinition="TEXT")
    private String lore;
}
