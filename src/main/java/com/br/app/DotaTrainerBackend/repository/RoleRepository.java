package com.br.app.DotaTrainerBackend.repository;

import com.br.app.DotaTrainerBackend.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    List<Role> findByDescriptionIn(List<String> description);
}
