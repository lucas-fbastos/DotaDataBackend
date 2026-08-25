package io.luck.DotaTrainerBackend.repository;

import io.luck.DotaTrainerBackend.model.entity.ProPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProPlayerRepository extends JpaRepository<ProPlayer,Long> {

}
