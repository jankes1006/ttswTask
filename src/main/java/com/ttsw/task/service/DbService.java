package com.ttsw.task.service;

import com.ttsw.task.domain.AppUser;
import com.ttsw.task.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbService {
    private final AppUserRepository appUserRepository;

    public AppUser createUser(AppUser appUser){
        return appUserRepository.save(appUser);
    }


}
