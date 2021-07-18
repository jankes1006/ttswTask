package com.ttsw.task.controller;

import com.ttsw.task.domain.user.AppUserRegisterDTO;
import com.ttsw.task.domain.user.AppUserToSendDTO;
import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Token;
import com.ttsw.task.enumVariable.user.CreateAccountResult;
import com.ttsw.task.enumVariable.user.ModifyFields;
import com.ttsw.task.exception.user.BadIdUserException;
import com.ttsw.task.exception.user.BadLoginProcess;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.mapper.user.AppUserMapper;
import com.ttsw.task.service.DbService;
import com.ttsw.task.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
    private final AppUserMapper appUserMapper;
    private final DbService dbService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private AppUser prepareNewUser(AppUserRegisterDTO appUserRegisterDTO) {
        AppUser appUser = appUserMapper.mapToAppUser(appUserRegisterDTO);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setRole("ROLE_USER");
        appUser.setEnabled(false);
        return appUser;
    }

    @PostMapping("/create")
    public CreateAccountResult create(@RequestBody AppUserRegisterDTO appUserRegisterDTO) {
        AppUser appUser = prepareNewUser(appUserRegisterDTO);

        if (dbService.getUserByEmail(appUser.getEmail()).size() > 0) {
            return CreateAccountResult.MAIL_EXIST;
        }
        if (dbService.getUserByUsername(appUser.getUsername()).isPresent()) {
            return CreateAccountResult.USERNAME_EXIST;
        }

        appUser = dbService.saveUser(appUser);
        Token token = new Token(appUser);
        dbService.saveToken(token);

        emailService.sendEmailToVerify(appUser, token);

        return CreateAccountResult.CREATE;
    }

    @GetMapping("/login")
    public AppUserToSendDTO login(@RequestParam String username, String password) throws BadLoginProcess {
        AppUser appUser = dbService.getUserByUsername(username).orElseThrow(BadLoginProcess::new);
        if (passwordEncoder.matches(password, appUser.getPassword())) {
            return appUserMapper.mapToAppUserToSendDTO(appUser);
        } else {
            throw new BadLoginProcess();
        }
    }

    @GetMapping("/getByEmail")
    public List<AppUserToSendDTO> getByEmail(@RequestParam String email) {
        return appUserMapper.mapToAppUserSendDTOList(dbService.getUserByEmail(email));
    }

    @GetMapping("/getByUsername")
    public AppUserToSendDTO getByUsername(@RequestParam String username) throws BadUsernameException {
        return appUserMapper.mapToAppUserToSendDTO(dbService.getUserByUsername(username).orElseThrow(BadUsernameException::new));
    }

    @PutMapping("/update")
    public AppUserToSendDTO update(@RequestParam Long id, String value, ModifyFields MODIFY_FIELDS) throws BadIdUserException {
        AppUser appUser = dbService.getUserById(id).orElseThrow(BadIdUserException::new);

        switch (MODIFY_FIELDS) {
            case EMAIL:
                appUser.setEmail(value);
                break;

            case PASSWORD:
                appUser.setPassword(passwordEncoder.encode(value));
                break;

            case ROLE:
                appUser.setRole(value);
        }
        return appUserMapper.mapToAppUserToSendDTO(dbService.saveUser(appUser));
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        dbService.deleteUserById(id);
    }

    @GetMapping("/verifyAccount")
    public void verifyAccount(@RequestParam String tokenValue) {
        Token token = dbService.findTokenByValue(tokenValue);
        AppUser appUser = token.getAppUser();
        appUser.setEnabled(true);
        dbService.saveUser(appUser);
        dbService.deleteToken(token);
        emailService.sendEmailThatVerifyIsCorrect(appUser);
    }
}
