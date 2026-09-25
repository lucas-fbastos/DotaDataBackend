package io.luck.DotaTrainerBackend.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() { }

    public User(Long userId, Player player, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean confirmed, String email) {
        this.userId = userId;
        this.player = player;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.confirmed = confirmed;
        this.email = email;
    }
}
