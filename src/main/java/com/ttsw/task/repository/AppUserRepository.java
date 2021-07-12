package com.ttsw.task.repository;

import com.ttsw.task.domain.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long>{
    @Override
    AppUser save(AppUser user);
    @Override
    void deleteById(Long aLong);

    Optional<AppUser> findById(Long aLong);
    AppUser findByUsername(String username);
    List<AppUser> findByEmail(String email);
}
