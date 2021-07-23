package com.ttsw.task.repository;

import com.ttsw.task.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {
    Optional<AppUser> findById(Long aLong);

    Optional<AppUser> findByUsername(String username);

    List<AppUser> findByEmail(String email);
}
