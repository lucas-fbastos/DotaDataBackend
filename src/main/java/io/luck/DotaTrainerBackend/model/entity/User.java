package io.luck.DotaTrainerBackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name="user", schema ="public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    @OneToOne
    @JoinColumn(name = "accountId", referencedColumnName = "accountId")
    private Player player;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean confirmed;
    private String email;
}
