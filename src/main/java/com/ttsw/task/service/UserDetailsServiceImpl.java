package com.ttsw.task.service;

import com.ttsw.task.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{
    private final AppUserRepository appUserRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return appUserRepository.findByUsername(username);
    }
}
