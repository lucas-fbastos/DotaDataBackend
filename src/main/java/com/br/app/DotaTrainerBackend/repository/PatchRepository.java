package com.br.app.DotaTrainerBackend.repository;

import com.br.app.DotaTrainerBackend.domain.Patch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatchRepository extends JpaRepository<Patch,Integer> {
}
