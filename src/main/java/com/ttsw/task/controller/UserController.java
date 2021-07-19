package com.ttsw.task.controller;

import com.ttsw.task.domain.user.AppUserRegisterDTO;
import com.ttsw.task.domain.user.AppUserToSendDTO;
import com.ttsw.task.enumVariable.user.CreateAccountResult;
import com.ttsw.task.enumVariable.user.ModifyFields;
import com.ttsw.task.exception.user.BadLoginProcess;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public CreateAccountResult create(@RequestBody AppUserRegisterDTO appUserRegisterDTO) {
        return userService.createAccount(appUserRegisterDTO);
    }

    @GetMapping("/login")
    public AppUserToSendDTO login(@RequestParam String username, String password) throws BadLoginProcess {
        return userService.login(username, password);
    }

    @GetMapping("/getByEmail")
    public List<AppUserToSendDTO> getByEmail(@RequestParam String email) {
        return userService.getByEmail(email);
    }

    @GetMapping("/getByUsername")
    public AppUserToSendDTO getByUsername(@RequestParam String username) throws BadUsernameException {
        return userService.getByUsername(username);
    }

    @PutMapping("/update")
    public AppUserToSendDTO update(@RequestParam String value, ModifyFields MODIFY_FIELDS, Principal principal) throws BadUsernameException {
        return userService.update(value, MODIFY_FIELDS, principal);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        userService.delete(id);
    }

    @GetMapping("/verifyAccount")
    public void verifyAccount(@RequestParam String tokenValue) {
        userService.verifyToken(tokenValue);
    }
}
