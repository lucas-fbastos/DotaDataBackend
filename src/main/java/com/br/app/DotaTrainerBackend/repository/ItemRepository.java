package com.br.app.DotaTrainerBackend.repository;

import com.br.app.DotaTrainerBackend.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
}
