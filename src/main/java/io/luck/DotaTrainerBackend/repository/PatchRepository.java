package io.luck.DotaTrainerBackend.repository;

import io.luck.DotaTrainerBackend.model.entity.Patch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatchRepository extends JpaRepository<Patch,Integer> {

    @Query("SELECT p FROM Patch p WHERE p.id = (SELECT MAX(p2.id) FROM Patch p2)")
    Patch getLatestPatch();

}
