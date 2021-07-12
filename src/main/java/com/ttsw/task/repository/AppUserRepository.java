package com.ttsw.task.repository;

import com.ttsw.task.domain.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUser, Long>{
    @Override
    AppUser save(AppUser user);

    AppUser findByUsername(String username);
}
