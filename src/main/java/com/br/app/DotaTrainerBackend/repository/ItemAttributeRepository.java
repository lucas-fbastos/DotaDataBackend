package com.br.app.DotaTrainerBackend.repository;

import com.br.app.DotaTrainerBackend.model.ItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemAttributeRepository extends JpaRepository<ItemAttribute,Long> {
}
