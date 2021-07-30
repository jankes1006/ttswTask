package com.ttsw.task.service;

import com.ttsw.task.domain.user.*;
import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Token;
import com.ttsw.task.enumVariable.offer.StateOffer;
import com.ttsw.task.enumVariable.user.CreateAccountResult;
import com.ttsw.task.enumVariable.user.ModifyFields;
import com.ttsw.task.enumVariable.user.PasswordResetResult;
import com.ttsw.task.exception.user.BadIdUserException;
import com.ttsw.task.exception.user.BadLoginProcess;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.mapper.user.AppUserMapper;
import com.ttsw.task.repository.AppUserRepository;
import com.ttsw.task.repository.TokenRepository;
import com.ttsw.task.service.log.LogService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserService {
    private final AppUserRepository appUserRepository;
    private final TokenRepository tokenRepository;
    private final AppUserMapper appUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final LogService logService;

    public AppUserToSendDTO login(String username, String password) throws BadLoginProcess {
        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(BadLoginProcess::new);
        if (passwordEncoder.matches(password, appUser.getPassword())) {
            return appUserMapper.mapToAppUserToSendDTO(appUser);
        } else {
            throw new BadLoginProcess();
        }
    }

    public TokenDTO loginToken(String username, String password) throws BadLoginProcess {
        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(BadLoginProcess::new);
        if (passwordEncoder.matches(password, appUser.getPassword())) {
            if (appUser.getRole().equals("ROLE_USER") || appUser.getRole().equals("ROLE_ADMIN")) {
                logService.addLogin(appUser);
            }
            long l = System.currentTimeMillis();
            String token = Jwts.builder()
                    .setSubject(appUser.getUsername())//odnosi sie do uzytkownika
                    .claim("role", appUser.getRole())//umieszczasz tu elementy klucz wartosc
                    .claim("name", appUser.getUsername())
                    .claim("password", appUser.getPassword())
                    .setIssuedAt(new Date(l))
                    //.setExpiration(new Date(l+2000000)) //token bedzie wazny 20 s
                    .signWith(SignatureAlgorithm.HS512, "key".getBytes()).compact();
            return new TokenDTO(token, appUser.getUsername(), appUser.getRole());
        } else {
            throw new BadLoginProcess();
        }
    }

    public CreateAccountResult createAccount(AppUserRegisterDTO appUserRegisterDTO) {
        AppUser appUser = appUserMapper.mapToAppUser(appUserRegisterDTO);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        if (appUserRepository.findByEmail(appUser.getEmail()).size() > 0) {
            return CreateAccountResult.MAIL_EXIST;
        }
        if (appUserRepository.findByUsername(appUser.getUsername()).isPresent()) {
            return CreateAccountResult.USERNAME_EXIST;
        }

        appUser = appUserRepository.save(appUser);
        Token token = new Token(appUser);
        tokenRepository.save(token);

        emailService.sendEmailToVerify(appUser, token);

        return CreateAccountResult.CREATE;
    }

    public void verifyToken(String tokenValue) {
        Token token = tokenRepository.findByValue(tokenValue);
        AppUser appUser = token.getAppUser();

        appUser.setEnabled(true);
        appUser.setRole("ROLE_USER");
        appUserRepository.save(appUser);

        tokenRepository.delete(token);
        emailService.sendEmailThatVerifyIsCorrect(appUser);
    }

    public AppUserToSendDTO update(String value, ModifyFields MODIFY_FIELDS, Principal principal) throws BadUsernameException {
        AppUser appUser = appUserRepository.findByUsername(principal.getName()).orElseThrow(BadUsernameException::new);
        switch (MODIFY_FIELDS) {
            case EMAIL:
                appUser.setEmail(value);
                break;

            case PASSWORD:
                appUser.setPassword(passwordEncoder.encode(value));
                break;
        }
        return appUserMapper.mapToAppUserToSendDTO(appUserRepository.save(appUser));
    }

    public List<AppUserToSendDTO> getByEmail(String email) {
        return appUserMapper.mapToAppUserSendDTOList(appUserRepository.findByEmail(email));
    }

    public AppUserToSendDTO getByUsername(@RequestParam String username) throws BadUsernameException {
        return appUserMapper.mapToAppUserToSendDTO(appUserRepository.findByUsername(username).orElseThrow(BadUsernameException::new));
    }

    public void delete(Long id) {
        appUserRepository.deleteById(id);
    }

    public List<AppUserToSendDTO> getAllUsers() {
        return appUserMapper.mapToAppUserSendDTOList((List<AppUser>) appUserRepository.findAll());
    }

    public AppUserToSendDTO getById(Long id) throws BadIdUserException {
        return appUserMapper.mapToAppUserToSendDTO(appUserRepository.findById(id).orElseThrow(BadIdUserException::new));
    }

    public AppUserToSendDTO update(AppUserUpdateAdminDTO appUserUpdateAdminDTO) throws BadIdUserException {
        AppUser appUser = appUserRepository.findById(appUserUpdateAdminDTO.getId()).orElseThrow(BadIdUserException::new);
        appUser.setRole(appUserUpdateAdminDTO.getRole());
        if (appUser.getRole().equals("ROLE_BAN")) {
            appUser.getUserOffers().forEach(offer -> offer.setStateOffer(StateOffer.BANNED));
        }
        return appUserMapper.mapToAppUserToSendDTO(appUserRepository.save(appUser));
    }

    public Page<AppUserToSendDTO> searchUser(Specification<AppUser> spec, Pageable pageable) {
        return appUserRepository.findAll(spec, pageable).map(appUserMapper::mapToAppUserToSendDTO);
    }

    public PasswordResetResult resetPassword(ResetPasswordDTO resetPasswordDTO) {
        Optional<AppUser> appUserOptional = appUserRepository.findByUsername(resetPasswordDTO.getUsername());
        if (!appUserOptional.isPresent()) return PasswordResetResult.NO_RESET;

        AppUser appUser = appUserOptional.get();

        if (!appUser.getEmail().equals(resetPasswordDTO.getEmail())) return PasswordResetResult.NO_RESET;
        if (appUser.getRole().equals("ROLE_UNCONFIRMED")) return PasswordResetResult.UNCONFIRMED_USER;

        Optional<Token> altToken = tokenRepository.findByAppUser(appUser);

        Token token = altToken.orElseGet(() -> new Token(appUser));

        tokenRepository.save(token);
        emailService.sendEmailResetPassword(appUser, token);

        return PasswordResetResult.RESET;
    }

    public AppUserToSendDTO getAppUserByToken(String token) {
        AppUser appUser = tokenRepository.findByValue(token).getAppUser();

        return appUserMapper.mapToAppUserToSendDTO(appUser);
    }

    public void setNewPassword(TokenPassword tokenPassword) {
        Token token = tokenRepository.findByValue(tokenPassword.getToken());
        AppUser appUser = token.getAppUser();
        appUser.setPassword(passwordEncoder.encode(tokenPassword.getNewPassword()));

        tokenRepository.delete(token);
    }
}
