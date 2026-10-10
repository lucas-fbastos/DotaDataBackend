package io.luck.AghsLabs.repository;

import io.luck.AghsLabs.model.entity.ProPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProPlayerRepository extends JpaRepository<ProPlayer,Long> {

}
