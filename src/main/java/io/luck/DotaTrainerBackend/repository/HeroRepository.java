package io.luck.DotaTrainerBackend.repository;

import io.luck.DotaTrainerBackend.model.entity.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends JpaRepository<Hero,Long> {
}
