package com.ttsw.task.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRegisterDTO {
    private String username;
    private String password;
    private String email;

    @Override
    public String toString() {
        return "AppUserDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
