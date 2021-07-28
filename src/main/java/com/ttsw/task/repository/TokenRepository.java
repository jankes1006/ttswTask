package com.ttsw.task.repository;

import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token,Long> {
    Token findByValue(String value);
    Optional<Token> findByAppUser(AppUser appUser);
}
