package io.luck.AghsLabs.repository;

import io.luck.AghsLabs.model.entity.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends JpaRepository<Hero,Long> {
}
