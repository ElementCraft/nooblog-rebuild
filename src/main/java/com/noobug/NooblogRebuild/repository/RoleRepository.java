package com.noobug.NooblogRebuild.repository;

import com.noobug.NooblogRebuild.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findOneByCodeAndDeleted(String code, Boolean deleted);
}
