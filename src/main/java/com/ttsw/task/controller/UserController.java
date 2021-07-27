package com.ttsw.task.controller;

import com.ttsw.task.domain.user.*;
import com.ttsw.task.entity.AppUser;
import com.ttsw.task.enumVariable.user.CreateAccountResult;
import com.ttsw.task.exception.user.BadIdUserException;
import com.ttsw.task.exception.user.BadLoginProcess;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.service.UserService;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/create") //nalozone aby moc sciagnac blokade
    public CreateAccountResult create(@RequestBody AppUserRegisterDTO appUserRegisterDTO) {
        return userService.createAccount(appUserRegisterDTO);
    }

    @GetMapping
    public AppUserToSendDTO login(String username, String password) throws BadLoginProcess {
        return userService.login(username, password);
    }

    @GetMapping("/login")
    public TokenDTO loginToken(String username, String password) throws BadLoginProcess {
        return userService.loginToken(username,password);
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

    @PutMapping
    public AppUserToSendDTO update(@RequestBody AppUserUpdateUserDTO appUserUpdateUserDTO, Principal principal) throws BadUsernameException {
        return userService.update(appUserUpdateUserDTO.getNewValue(), appUserUpdateUserDTO.getModifyFields(), principal);
    }

    @PutMapping("/updateAdmin")
    public AppUserToSendDTO update(@RequestBody AppUserUpdateAdminDTO appUserUpdateAdminDTO, Principal principal) throws BadIdUserException {
        return userService.update(appUserUpdateAdminDTO);
    }

    @DeleteMapping
    public void delete(@RequestParam Long id) {
        userService.delete(id);
    }

    @GetMapping("/verifyAccount")
    public void verifyAccount(@RequestParam String tokenValue) {
        userService.verifyToken(tokenValue);
    }

    @GetMapping(value = "/getPageable")
    public Page<AppUserToSendDTO> getPageableUsers(
            @And({
                    @Spec(path = "username", params = "username", spec = Like.class),
                    @Spec(path = "email", params = "email", spec = Like.class),
                    @Spec(path = "role", params = "role", spec = Like.class)
            }) Specification<AppUser> spec, Pageable pageable) {
        return userService.searchUser(spec, pageable);
    }

    @GetMapping("/getAll")
    public List<AppUserToSendDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}
