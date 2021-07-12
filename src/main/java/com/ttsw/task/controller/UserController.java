package com.ttsw.task.controller;

import com.ttsw.task.domain.AppUser;
import com.ttsw.task.domain.AppUserRegisterDTO;
import com.ttsw.task.domain.AppUserToSendDTO;
import com.ttsw.task.mapper.AppUserMapper;
import com.ttsw.task.service.DbService;
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

    @PostMapping("/createUser")
    public AppUserToSendDTO createUser(@RequestBody AppUserRegisterDTO appUserRegisterDTO){
        AppUser appUser = AppUserMapper.INSTANCE.mapToAppUser(appUserRegisterDTO);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setRole("ROLE_USER");

        return AppUserMapper.INSTANCE.mapToAppUserToSendDTO(dbService.createUser(appUser));
    }

    @GetMapping("/getUserByEmail")
    public List<AppUser> getUsersByEmail(@RequestParam String email){
        return dbService.getByEmail(email);
    }

    @GetMapping("/getUserByUsername")
    public AppUser getUserByUsername(@RequestParam String username){
        return dbService.getByUsername(username);
    }

    @PutMapping("/updateUser")
    public AppUser updateUser(@RequestParam Long id, String value, ModifyFields MODIFY_FIELDS) throws BadIdUserException{

        Optional<AppUser> optionalAppUser = dbService.getById(id);
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
        return dbService.createUser(appUser);
    }

    @DeleteMapping("/deleteUser")
    public void deleteUser(long id){
        dbService.deleteById(id);
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
