package com.br.app.DotaTrainerBackend.repository;

import com.br.app.DotaTrainerBackend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {
}
