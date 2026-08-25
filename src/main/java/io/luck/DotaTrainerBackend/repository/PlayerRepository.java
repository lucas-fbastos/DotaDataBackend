package io.luck.DotaTrainerBackend.repository;

import io.luck.DotaTrainerBackend.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {
}
