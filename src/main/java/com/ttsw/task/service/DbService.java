package com.ttsw.task.service;

import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Offer;
import com.ttsw.task.entity.Token;
import com.ttsw.task.repository.AppUserRepository;
import com.ttsw.task.repository.OfferRepository;
import com.ttsw.task.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbService {
    private final AppUserRepository appUserRepository;
    private final TokenRepository tokenRepository;
    private final OfferRepository offerRepository;

    public AppUser saveUser(AppUser appUser){
        return appUserRepository.save(appUser);
    }

    public List<AppUser> getUserByEmail(String email){
        return appUserRepository.findByEmail(email);
    }

    public Optional<AppUser> getUserByUsername(String username){
        return appUserRepository.findByUsername(username);
    }

    public Optional<AppUser> getUserById(Long id){
        return appUserRepository.findById(id);
    }

    public void deleteUserById(Long id){
        appUserRepository.deleteById(id);
    }



    public Token saveToken(Token token){ return tokenRepository.save(token);}

    public Token findTokenByValue(String tokenValue){ return tokenRepository.findByValue(tokenValue);}

    public void deleteToken(Token token){
        tokenRepository.delete(token);
    }


    public Offer saveOffer(Offer offer){
        return offerRepository.save(offer);
    }

    public List<Offer> getAllOffer(){return (List<Offer>) offerRepository.findAll();}

    public Optional<Offer> getOfferById(Long id){
        return offerRepository.findById(id);
    }
}
