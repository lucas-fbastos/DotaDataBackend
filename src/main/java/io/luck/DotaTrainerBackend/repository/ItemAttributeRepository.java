package io.luck.DotaTrainerBackend.repository;

import io.luck.DotaTrainerBackend.model.entity.ItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemAttributeRepository extends JpaRepository<ItemAttribute,Long> {
}
