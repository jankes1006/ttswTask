package com.ttsw.task.domain;

public class AppUserDTO {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "AppUserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
