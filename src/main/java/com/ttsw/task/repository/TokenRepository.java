package com.ttsw.task.repository;

import com.ttsw.task.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token,Long> {
    Token findByValue(String value);
}
