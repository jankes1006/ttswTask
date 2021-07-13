package com.ttsw.task.controller;

import com.ttsw.task.domain.*;
import com.ttsw.task.mapper.AppUserMapper;
import com.ttsw.task.service.DbService;
import com.ttsw.task.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/v1/ttswTask")
@RestController
public class UserController {
    private final DbService dbService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AppUser prepareNewUser(AppUserRegisterDTO appUserRegisterDTO){
        AppUser appUser = AppUserMapper.INSTANCE.mapToAppUser(appUserRegisterDTO);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setRole("ROLE_USER");
        appUser.setEnabled(false);
        return appUser;
    }

    @PostMapping("/createUser")
    public AppUserToSendDTO createUser(@RequestBody AppUserRegisterDTO appUserRegisterDTO){
        AppUser appUser = prepareNewUser(appUserRegisterDTO);
        appUser=dbService.saveUser(appUser);

        Token token = new Token(appUser);
        dbService.saveToken(token);

        emailService.sendEmailToVerify(appUser,token);

        return AppUserMapper.INSTANCE.mapToAppUserToSendDTO(appUser);
    }

    @GetMapping("/getUserByEmail")
    public List<AppUser> getUsersByEmail(@RequestParam String email){
        return dbService.getUserByEmail(email);
    }

    @GetMapping("/getUserByUsername")
    public AppUser getUserByUsername(@RequestParam String username){
        return dbService.getUserByUsername(username);
    }

    @PutMapping("/updateUser")
    public AppUser updateUser(@RequestParam Long id, String value, ModifyFields MODIFY_FIELDS) throws BadIdUserException{

        Optional<AppUser> optionalAppUser = dbService.getUserById(id);
        if(!optionalAppUser.isPresent()){ //czy w ten sposob to rozwiazac?
            throw new BadIdUserException();
        }
        AppUser appUser=optionalAppUser.get();

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
        return dbService.saveUser(appUser);
    }

    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestParam Long id){
        dbService.deleteUserById(id);
    }

    @GetMapping("/verifyAccount")
    public void verifyAccount(@RequestParam String tokenValue){
        Token token = dbService.findTokenByValue(tokenValue);
        AppUser appUser = token.getAppUser();
        appUser.setEnabled(true);
        dbService.saveUser(appUser);
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
