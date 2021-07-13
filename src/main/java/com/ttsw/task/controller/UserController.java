package com.ttsw.task.controller;

import com.ttsw.task.domain.*;
import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Token;
import com.ttsw.task.enumVariable.ModifyFields;
import com.ttsw.task.exception.BadIdUserException;
import com.ttsw.task.exception.BadUsernameException;
import com.ttsw.task.mapper.AppUserMapper;
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

    public AppUser prepareNewUser(AppUserRegisterDTO appUserRegisterDTO){
        AppUser appUser = appUserMapper.mapToAppUser(appUserRegisterDTO);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setRole("ROLE_USER");
        appUser.setEnabled(false);
        return appUser;
    }

    @PostMapping("/create")
    public AppUserToSendDTO createUser(@RequestBody AppUserRegisterDTO appUserRegisterDTO){
        AppUser appUser = prepareNewUser(appUserRegisterDTO);
        appUser=dbService.saveUser(appUser);

        Token token = new Token(appUser);
        dbService.saveToken(token);

        emailService.sendEmailToVerify(appUser,token);

        return appUserMapper.mapToAppUserToSendDTO(appUser);
    }

    @GetMapping("/getByEmail")
    public List<AppUserToSendDTO> getUsersByEmail(@RequestParam String email){
        return appUserMapper.mapToAppUserSendDTOList(dbService.getUserByEmail(email));
    }

    @GetMapping("/getByUsername")
    public AppUserToSendDTO getUserByUsername(@RequestParam String username) throws BadUsernameException {
        return appUserMapper.mapToAppUserToSendDTO(dbService.getUserByUsername(username).orElseThrow(BadUsernameException::new));
    }

    @PutMapping("/update")
    public AppUserToSendDTO updateUser(@RequestParam Long id, String value, ModifyFields MODIFY_FIELDS) throws BadIdUserException {
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
    public void deleteUser(@RequestParam Long id){
        dbService.deleteUserById(id);
    }

    @GetMapping("/verify")
    public void verifyAccount(@RequestParam String tokenValue){
        Token token = dbService.findTokenByValue(tokenValue);
        AppUser appUser = token.getAppUser();
        appUser.setEnabled(true);
        dbService.saveUser(appUser);
        dbService.deleteToken(token);
        emailService.sendEmailThatVerifyIsCorrect(appUser);
    }

    @GetMapping("/sayHello")
    public String hello(){
        return "HELLO";
    }

    @GetMapping("/sayHello2")
    public String hello2(){
        return "HELLO2";
    }
}
