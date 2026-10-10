package io.luck.AghsLabs.repository;

import io.luck.AghsLabs.model.entity.ItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemAttributeRepository extends JpaRepository<ItemAttribute,Long> {
}
