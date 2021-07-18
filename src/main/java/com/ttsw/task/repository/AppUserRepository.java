package com.ttsw.task.repository;

import com.ttsw.task.entity.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findById(Long aLong);

    Optional<AppUser> findByUsername(String username);

    List<AppUser> findByEmail(String email);
}
