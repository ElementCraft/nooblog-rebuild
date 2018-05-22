package com.noobug.NooblogRebuild.repository;

import com.noobug.NooblogRebuild.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAccountAndDeleted(String account, Boolean isDeleted);

    Optional<User> findByIdAndDeleted(Long id, Boolean isDeleted);

    Optional<User> findByAccountAndPasswordAndBannedAndDeleted(String account, String password, Boolean isBanned, Boolean isDeleted);

    Page<User> findAllByDeleted(Boolean deleted, Pageable pageable);
}
