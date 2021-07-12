package com.ttsw.task.service;

import com.ttsw.task.domain.AppUser;
import com.ttsw.task.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbService {
    private final AppUserRepository appUserRepository;

    public AppUser createUser(AppUser appUser){
        return appUserRepository.save(appUser);
    }

    public List<AppUser> getByEmail(String email){
        return appUserRepository.findByEmail(email);
    }

    public AppUser getByUsername(String username){
        return appUserRepository.findByUsername(username);
    }

    public Optional<AppUser> getById(Long id){
        return appUserRepository.findById(id);
    }

    public void deleteById(Long id){
        appUserRepository.deleteById(id);
    }

}
