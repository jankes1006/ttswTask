package com.ttsw.task.service;

import com.ttsw.task.domain.user.AppUserRegisterDTO;
import com.ttsw.task.domain.user.AppUserToSendDTO;
import com.ttsw.task.domain.user.AppUserUpdateAdminDTO;
import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Token;
import com.ttsw.task.enumVariable.user.CreateAccountResult;
import com.ttsw.task.enumVariable.user.ModifyFields;
import com.ttsw.task.exception.user.BadIdUserException;
import com.ttsw.task.exception.user.BadLoginProcess;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.mapper.user.AppUserMapper;
import com.ttsw.task.repository.AppUserRepository;
import com.ttsw.task.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UserService {
    private final AppUserRepository appUserRepository;
    private final TokenRepository tokenRepository;
    private final AppUserMapper appUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AppUserToSendDTO login(String username, String password) throws BadLoginProcess {
        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(BadLoginProcess::new);
        if (passwordEncoder.matches(password, appUser.getPassword())) {
            return appUserMapper.mapToAppUserToSendDTO(appUser);
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
        return appUserMapper.mapToAppUserToSendDTO(appUserRepository.save(appUser));
    }
}
