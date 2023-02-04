package com.br.app.DotaTrainerBackend.repository;

import com.br.app.DotaTrainerBackend.model.ProPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProPlayerRepository extends JpaRepository<ProPlayer,Long> {

}
