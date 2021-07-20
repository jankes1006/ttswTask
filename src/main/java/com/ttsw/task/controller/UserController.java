package com.ttsw.task.controller;

import com.ttsw.task.domain.user.AppUserRegisterDTO;
import com.ttsw.task.domain.user.AppUserToSendDTO;
import com.ttsw.task.domain.user.AppUserUpdateAdminDTO;
import com.ttsw.task.domain.user.AppUserUpdateUserDTO;
import com.ttsw.task.enumVariable.user.CreateAccountResult;
import com.ttsw.task.exception.user.BadIdUserException;
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

    @GetMapping("/getById")
    public AppUserToSendDTO getById(@RequestParam Long id) throws BadIdUserException {
        return userService.getById(id);
    }

    @PutMapping("/update")
    public AppUserToSendDTO update(@RequestBody AppUserUpdateUserDTO appUserUpdateUserDTO, Principal principal) throws BadUsernameException {
        return userService.update(appUserUpdateUserDTO.getNewValue(), appUserUpdateUserDTO.getModifyFields(), principal);
    }

    @PutMapping("/updateAdmin")
    public AppUserToSendDTO update(@RequestBody AppUserUpdateAdminDTO appUserUpdateAdminDTO, Principal principal) throws BadIdUserException {
        return userService.update(appUserUpdateAdminDTO);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        userService.delete(id);
    }

    @GetMapping("/verifyAccount")
    public void verifyAccount(@RequestParam String tokenValue) {
        userService.verifyToken(tokenValue);
    }

    @GetMapping("/getAll")
    public List<AppUserToSendDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}
