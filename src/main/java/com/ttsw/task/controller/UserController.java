package com.ttsw.task.controller;

import com.ttsw.task.config.WebSecurityConfig;
import com.ttsw.task.domain.AppUser;
import com.ttsw.task.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/v1/ttswTask")
@RestController
public class UserController {
    private final DbService dbService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/createUser")
    public AppUser createUser(@RequestBody AppUser appUser){
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return dbService.createUser(appUser);
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
