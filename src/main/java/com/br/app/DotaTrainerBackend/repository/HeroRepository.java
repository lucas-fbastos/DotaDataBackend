package com.br.app.DotaTrainerBackend.repository;

import com.br.app.DotaTrainerBackend.domain.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends JpaRepository<Hero,Integer> {
}
