package com.ttsw.task.repository;

import com.ttsw.task.domain.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token,Long> {
    @Override
    Token save(Token token);

    Token findByValue(String value);
}
